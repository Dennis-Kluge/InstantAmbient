# -*- encoding: utf-8 -*-
$:.push File.expand_path("../lib", __FILE__)
require "AmbientActor/version"

Gem::Specification.new do |s|
  s.name        = "AmbientActor"
  s.version     = AmbientActor::VERSION
  s.authors     = ["Horst Mumpitz"]
  s.email       = ["dennis.kluge@gmx.de"]
  s.homepage    = ""
  s.summary     = %q{TODO: Write a gem summary}
  s.description = %q{TODO: Write a gem description}

  s.rubyforge_project = "AmbientActor"

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

end
