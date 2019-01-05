package com.apnihaveli.artiStick;

import com.apnihaveli.artiStick.auth.UserAuthenticator;
import com.apnihaveli.artiStick.consumers.ImageBufferConsumer;
import com.apnihaveli.artiStick.dao.UserAuthDao;
import com.apnihaveli.artiStick.dao.UserImageDao;
import com.apnihaveli.artiStick.dao.UserDao;
import com.apnihaveli.artiStick.health.TemplateHealthCheck;
import com.apnihaveli.artiStick.model.User;
import com.apnihaveli.artiStick.resources.HelloWorldResource;
import com.apnihaveli.artiStick.resources.ImageResource;
import com.apnihaveli.artiStick.resources.UserResource;
import com.apnihaveli.artiStick.services.*;
import com.flickr4java.flickr.Flickr;
import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.REST;
import com.flickr4java.flickr.auth.Auth;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import io.dropwizard.Application;
import io.dropwizard.auth.AuthDynamicFeature;
import io.dropwizard.auth.AuthValueFactoryProvider;
import io.dropwizard.auth.oauth.OAuthCredentialAuthFilter;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.jdbi3.JdbiFactory;
import io.dropwizard.migrations.MigrationsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.jdbi.v3.core.Jdbi;
import org.scribe.model.Token;

import java.io.IOException;
import java.util.concurrent.TimeoutException;


public class ArtiStickApplication extends Application<ArtiStickConfiguration> {

    public static void main(String[] args) throws Exception{
        new ArtiStickApplication().run(args);
    }

    @Override
    public String getName(){
        return "artiStick";
    }

    @Override
    public void initialize(Bootstrap<ArtiStickConfiguration> bootstrap) {
        bootstrap.addBundle(new MigrationsBundle<ArtiStickConfiguration>() {

            public DataSourceFactory getDataSourceFactory(ArtiStickConfiguration configuration) {
                return configuration.getDataSourceFactory();
            }
        });
    }

    @Override
    public void run(ArtiStickConfiguration artiStickConfiguration, Environment environment) throws IOException, TimeoutException, FlickrException{
        final JdbiFactory factory = new JdbiFactory();

        final Jdbi jdbi = factory.build(environment, artiStickConfiguration.getDataSourceFactory(), "postgresql");

        final Connection AMQPConnection =  createAMQPConnection(artiStickConfiguration.getAmqpConfig());

        final ImageService imageService = new ImageService(jdbi.onDemand(UserImageDao.class), AMQPConnection.createChannel());

        final UserAuthService userAuthService = new UserAuthService(jdbi.onDemand(UserAuthDao.class));

        final UserService userService = new UserService(jdbi.onDemand(UserDao.class), userAuthService);

        final FlickrConfig flickrConfig = artiStickConfiguration.getFlickrConfig();

        final Flickr adminFlickr = new Flickr(flickrConfig.getApiKey(), flickrConfig.getApiSecret(), new REST());

        final Auth adminFlickrAuth = adminFlickr.getAuthInterface().checkToken(new Token(flickrConfig.getAccessToken(), flickrConfig.getAccessTokenSecret()));

        final HelloWorldResource hwResource = new HelloWorldResource(
                artiStickConfiguration.getTemplate(),
                artiStickConfiguration.getDefaultName()
        );

        final UserResource userResource = new UserResource(userService, adminFlickr);

        final ImageResource imageResource = new ImageResource(imageService);


        final TemplateHealthCheck healthCheck = new TemplateHealthCheck(
                artiStickConfiguration.getTemplate()
        );

        final QueueService queueService = new QueueService(new ImageBufferConsumer(imageService), adminFlickr, adminFlickrAuth);

        environment.jersey().register(new AuthDynamicFeature(
                new OAuthCredentialAuthFilter.Builder<User>()
                .setAuthenticator(new UserAuthenticator(userService, userAuthService))
                .buildAuthFilter()
        ));
        environment.jersey().register(new AuthValueFactoryProvider.Binder<>(User.class));
        environment.jersey().register(hwResource);
        environment.jersey().register(userResource);
        environment.jersey().register(imageResource);
        environment.jersey().register(MultiPartFeature.class);
        environment.healthChecks().register("template", healthCheck);

        queueService.addConsumer(AMQPConnection.createChannel(), QueueNames.DB_TO_FLICKR.toString());
    }

    private Connection createAMQPConnection(AMQPConfiguration config) throws TimeoutException, IOException{
        ConnectionFactory factory = new ConnectionFactory();
        factory.setConnectionTimeout(1000);//times out in 1s.
        factory.setUsername(config.getUsername());
        factory.setPassword(config.getPassword());
        factory.setHost(config.getHost());
        factory.setPort(config.getPort());

        return factory.newConnection();
    }
}
