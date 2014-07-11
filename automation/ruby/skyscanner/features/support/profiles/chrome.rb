require File.dirname(__FILE__) + '/../env.rb'

Capybara.current_driver = :selenium
Capybara.register_driver :selenium do |driver|
  chrome_driver_path = @drivers_path + 'chrome/'

  if @host_platform.linux?
    if @host_platform.bitsize == 64
      Selenium::WebDriver::Chrome.driver_path = chrome_driver_path + 'chrome_linux64'
    else
      Selenium::WebDriver::Chrome.driver_path = chrome_driver_path + 'chrome_linux32'
    end
  elsif @host_platform.mac?
    Selenium::WebDriver::Chrome.driver_path = chrome_driver_path + 'chrome_macosx'
  else
    Selenium::WebDriver::Chrome.driver_path = chrome_driver_path + 'chrome_win.exe'
  end

  options = {
      browser: :chrome
  }
  Capybara::Selenium::Driver.new(driver, options)
end