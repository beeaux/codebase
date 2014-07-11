require File.dirname(__FILE__) + '/../env'

Capybara.default_driver = :selenium
Capybara.register_driver :selenium do |driver|
  ie_driver_path = @drivers_path + 'ie/'

  (@host_platform.bitsize == 64) ?
      Selenium::WebDriver::IE.driver_path = ie_driver_path + 'ie_x64.exe' :
      Selenium::WebDriver::IE.driver_path = ie_driver_path + 'ie_x32.exe'

  options = {
      browser: :ie
  }
  Capybara::Selenium::Driver.new(driver, options)
end
