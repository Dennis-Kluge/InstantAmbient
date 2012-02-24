# -*- encoding: utf-8 -*-
$:.push File.expand_path("../lib", __FILE__)
require "AmbientBrain/version"

Gem::Specification.new do |s|
  s.name        = "AmbientBrain"
  s.version     = AmbientBrain::VERSION
  s.authors     = ["Horst Mumpitz"]
  s.email       = ["dennis.kluge@gmx.de"]
  s.homepage    = ""
  s.summary     = %q{TODO: Write a gem summary}
  s.description = %q{TODO: Write a gem description}

  s.rubyforge_project = "AmbientBrain"

  s.files         = `git ls-files`.split("\n")
  s.test_files    = `git ls-files -- {test,spec,features}/*`.split("\n")
  s.executables   = `git ls-files -- bin/*`.split("\n").map{ |f| File.basename(f) }
  s.require_paths = ["lib"]

  s.add_dependency "json"
  s.add_dependency "amqp"

  s.add_development_dependency "guard"
  s.add_development_dependency "rspec"
  s.add_development_dependency "ruby_gntp"
  s.add_development_dependency "guard-rspec"
  s.add_development_dependency "guard-livereload"

end
