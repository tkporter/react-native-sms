require 'json'
package_json = JSON.parse(File.read('package.json'))

Pod::Spec.new do |s|

   s.name           = "react-native-sms"
   s.version        = package_json["version"]
   s.summary        = package_json["description"]
   s.homepage       = "https://github.com/tkporter/react-native-sms"
   s.license        = package_json["license"]
   s.author         = { package_json["author"] => package_json["author"] }
   s.platform       = :ios, "8.0"
   s.source         = { :git => "#{package_json["repository"]["url"]}" }
   s.source_files   = 'SendSMS/*.{h,m}'
   s.dependency 'React'

end