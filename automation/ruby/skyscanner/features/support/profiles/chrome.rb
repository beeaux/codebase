require File.dirname(__FILE__) + '/../env'

Capybara.default_driver = :selenium
Capybara.register_driver :selenium do |driver|
  chrome_driver_path = @drivers_path + 'chrome/'

  if @host_platform.linux?
    (@host_platform.bitsize == 64) ?
        Selenium::WebDriver::Chrome.driver_path = chrome_driver_path + 'chrome_linux64' :
        Selenium::WebDriver::Chrome.driver_path = chrome_driver_path + 'chrome_linux32'
  elsif @host_platform.mac?
    Selenium::WebDriver::Chrome.driver_path = chrome_driver_path + 'chrome_macosx'
  else
    Selenium::WebDriver::Chrome.driver_path = chrome_driver_path + 'chrome_win.exe'
  end

  options = {
      browser: :chrome,
      :switches => %w[--ignore-certificate-errors --disable-popup-blocking --disable-translate]
  }
  Capybara::Selenium::Driver.new(driver, options)
end