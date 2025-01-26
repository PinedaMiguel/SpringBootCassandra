package com.project.cassandra.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.cassandra.config.AbstractCassandraConfiguration;
import org.springframework.data.cassandra.config.CqlSessionFactoryBean;
import org.springframework.data.cassandra.config.SchemaAction;
import org.springframework.data.cassandra.core.cql.keyspace.CreateKeyspaceSpecification;
import org.springframework.data.cassandra.core.cql.keyspace.KeyspaceOption;
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories;

import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;

@Configuration
@EnableCassandraRepositories(basePackages = "com.project.cassandra.repository")
public class CassandraConfig extends AbstractCassandraConfiguration
{

    @Value("${spring.cassandra.keyspace-name}")
    private String keyspaceName;

    @Value("${spring.cassandra.contact-points}")
    private String contactPoints;

    @Value("${spring.cassandra.local-datacenter}")
    private String localDatacenter;

    @Value("${spring.cassandra.port}")
    private int port;

    @Override
    protected String getKeyspaceName()
    {
        return keyspaceName;
    }

    @Override
    protected String getContactPoints()
    {
        return contactPoints;
    }

    @Override
    protected int getPort()
    {
        return port;
    }

    @Override
    protected String getLocalDataCenter()
    {
        return localDatacenter;
    }

    @Override
    public SchemaAction getSchemaAction()
    {
        return SchemaAction.CREATE_IF_NOT_EXISTS;
    }

    @Override
    protected List<CreateKeyspaceSpecification> getKeyspaceCreations()
    {
        CreateKeyspaceSpecification specification = CreateKeyspaceSpecification
                .createKeyspace(keyspaceName)
                .ifNotExists()
                .with(KeyspaceOption.DURABLE_WRITES, true)
                .withSimpleReplication(1);
        return List.of(specification);
    }

    @Bean
    @Override
    public CqlSessionFactoryBean cassandraSession()
    {
        try
        {
            CqlSessionFactoryBean cassandraSession = super.cassandraSession();
            Resource schema = new ClassPathResource("schema.cql");
            String schemaContent = new String(Files.readAllBytes(schema.getFile().toPath()));
            cassandraSession.setStartupScripts(Arrays.asList(schemaContent.split(";")));
            return cassandraSession;
        }
        catch (Exception e)
        {
            throw new RuntimeException("Failed to load schema.cql", e);
        }
    }
}