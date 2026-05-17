package dev.revere.delta.api.service;

import dev.revere.delta.util.CC;
import org.bukkit.Bukkit;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Remi
 * @project Delta
 * @date 6/17/2024
 */
public class ServiceManager {
    private final Map<Class<? extends IService>, IService> services = new LinkedHashMap<>();

    /**
     * Register a service
     *
     * @param service the service to register
     */
    public void registerService(IService service) {
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
    public <T extends IService> T getService(Class<T> serviceClass) {
        return (T) services.get(serviceClass);
    }

    /**
     * Register all services
     */
    public void registerAllServices() {
        for (IService service : services.values()) {
            long duration = measureExecutionTime(service::register);
            Bukkit.getConsoleSender().sendMessage(CC.translate("&fRegistered service &b" + service.getClass().getSimpleName() + " &fin &b" + duration + "ms"));
        }
    }

    /**
     * Measure the execution time of a runnable
     *
     * @param runnable the runnable to measure
     * @return the execution time
     */
    private long measureExecutionTime(Runnable runnable) {
        long start = System.currentTimeMillis();
        runnable.run();
        return System.currentTimeMillis() - start;
    }
}
