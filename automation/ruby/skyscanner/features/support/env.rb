require 'capybara'
require 'selenium-webdriver'
require 'cucumber'
require 'capybara/poltergeist'
require 'capybara/dsl'
require 'capybara/cucumber'

@drivers_path = File.dirname(__FILE__) + '/drivers/'
@host_platform = Selenium::WebDriver::Platform

# set after condition to capture screenshot for failed scenario
After { |scenario|
  if scenario.failed?
    page.driver.browser.save_screenshot("#{scenario.__id__}.png")
    embed("#{scenario.__id__}.png", 'image/png', 'screenshot')
  end
}