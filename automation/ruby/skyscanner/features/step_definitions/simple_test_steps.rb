Given /^I navigate to (.*?)$/ do |website|
  visit 'http://www.' + website + '.com'
end