package com.infy.driverFactory;

import org.aeonbits.owner.Config;

@Config.Sources("file:${user.dir}/src/main/resources/AutomationConfig.properties")

public interface AutomationConfig extends Config {
    
	@DefaultValue("chrome")
    @Key("browser")
    String browser();

    @Key("selenium.grid.url")
    String seleniumGridUrl();
}

