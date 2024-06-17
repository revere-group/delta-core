package dev.revere.revsentials.api.service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Remi
 * @project Revsential
 * @date 6/17/2024
 */
public class ServiceManager {
    private final Map<Class<? extends Service>, Service> services = new HashMap<>();

    /**
     * Register a service
     *
     * @param service the service to register
     */
    public void registerService(Service service) {
        services.put(service.getClass(), service);
    }

    /**
     * Get a service by its class
     *
     * @param serviceClass the class of the service
     * @param <T>          the type of the service
     * @return the service
     */
    @SuppressWarnings("unchecked")
    public <T extends Service> T getService(Class<T> serviceClass) {
        return (T) services.get(serviceClass);
    }

    /**
     * Register all services
     */
    public void registerAllServices() {
        for (Service service : services.values()) {
            service.register();
        }
    }
}
