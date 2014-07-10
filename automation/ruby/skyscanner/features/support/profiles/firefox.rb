require File.dirname(__FILE__) + '/../env.rb'

Capybara.current_driver = :selenium
Capybara.register_driver(:selenium) { |driver|
  options = {
      browser: :firefox
  }

  Capybara::Selenium::Driver.new(driver, options)
}