require File.dirname(__FILE__) + '/../env'

Capybara.default_driver = :selenium
Capybara.register_driver :selenium do |driver|
  options = {
      browser: :firefox
  }
  Capybara::Selenium::Driver.new(driver, options)
end