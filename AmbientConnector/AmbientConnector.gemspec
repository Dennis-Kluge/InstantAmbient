# -*- encoding: utf-8 -*-
$:.push File.expand_path("../lib", __FILE__)
require "AmbientConnector/version"

Gem::Specification.new do |s|
  s.name        = "ambient-connector"
  s.version     = AmbientConnector::VERSION
  s.authors     = ["Horst Mumpitz"]
  s.email       = ["denniskluge@me.com"]
  s.homepage    = "http://github.com/horstmumpitz"
  s.summary     = "Bluetooth connector for the instant ambient project. "
  s.description = ""

  s.rubyforge_project = "AmbientConnector"

  s.files         = `git ls-files`.split("\n")
  s.test_files    = `git ls-files -- {test,spec,features}/*`.split("\n")
  s.executables   = `git ls-files -- bin/*`.split("\n").map{ |f| File.basename(f) }
  s.require_paths = ["lib"]

  s.add_dependency "json_pure"  

  s.add_development_dependency "guard"
  s.add_development_dependency "rspec"
  s.add_development_dependency "ruby_gntp"
  s.add_development_dependency "guard-rspec"
  s.add_development_dependency "guard-livereload"
end
