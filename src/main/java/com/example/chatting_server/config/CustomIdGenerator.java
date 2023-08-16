package com.example.chatting_server.config;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.enhanced.SequenceStyleGenerator;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.type.Type;

import java.io.Serializable;
import java.util.Properties;

public class CustomIdGenerator extends SequenceStyleGenerator {
    private String prefix;

    @Override
    public void configure(Type type, Properties params, ServiceRegistry serviceRegistry)  {
        super.configure(type, params, serviceRegistry);
        prefix = (String) params.get("prefix");
    }

    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object object) throws HibernateException {

        return prefix + System.currentTimeMillis();
    }
}
