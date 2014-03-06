package com.postbord;

public class SharedRemoteWebDriver extends EventFiringWebDriver {
  private static RemoteWebDriver SHARED_DRIVER;
  private static DesiredCapabilities capabilities;
  
  private static final Thread CLOSE_THREAD = new Thread() {
    @Override
    public void run() {
      SHARED_DRIVER.close();
    }
  }
  
  static {
    Runtime.getRuntime().addShutdownHook(CLOSE_THREAD);
  }
  
  public SharedRemoteWebDriver() {
    super(SHARED_DRIVER);
  }
  
  @Override
  public void close() {
    if(Thread.currentThread() != CLOSE_THREAD) {
      throw new UnsupportedOperationException("You shouldn't close this WebDriver. It's shared and will close when the JVM exits.");
      
      super.close();
    }
  }
  
  @Before
  public void deleteAllCookies() {
    manage().deleteAllCookies();
  }
  
  private RemoteWebDriver setRemoteWebDriverToChrome() throws MalformedURLException {
    String chromeDriverExecutable = getDriverExecutable("chromedriver");
    
    DriverService driverService = new ChromeDriverService.Builder()
        .usingDriverExecutable(new File(chromeDriverExecutable))
        .usingAnyFreePort()
        .withLogFile(new File("chromedriver.log"))
        .build();
        
    try {
      driverService.start();
    } catch(IOException e) {
      throw e;
    }
    
    capabilities = DesiredCapabilities.chrome();
    
    return new RemoteWebDriver(driverService.getUrl(), capabilities);
  }
  
}
