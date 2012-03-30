# -*- encoding: utf-8 -*-
$:.push File.expand_path("../lib", __FILE__)
require "ambient_brain/version"

Gem::Specification.new do |s|
  s.name        = "AmbientBrain"
  s.version     = AmbientBrain::VERSION
  s.authors     = ["Horst Mumpitz"]
  s.email       = ["dennis.kluge@gmx.de"]
  s.homepage    = "https://github.com/HorstMumpitz/InstantAmbient"
  s.summary     = %q{Configuration and routing tool for environments}
  s.description = %q{This Gem is part of the InstantAmbient project}

  s.rubyforge_project = "AmbientBrain"

  s.files         = `git ls-files`.split("\n")
  s.test_files    = `git ls-files -- {test,spec,features}/*`.split("\n")
  s.executables   = `git ls-files -- bin/*`.split("\n").map{ |f| File.basename(f) }
  s.require_paths = ["lib"]

  s.add_dependency "json"
  s.add_dependency "xml-simple"
  s.add_dependency "eventmachine"

  s.add_development_dependency "guard"
  s.add_development_dependency "rspec"
  s.add_development_dependency "ruby_gntp"
  s.add_development_dependency "guard-rspec"
  s.add_development_dependency "guard-livereload"
  s.add_development_dependency "simplecov"

end
