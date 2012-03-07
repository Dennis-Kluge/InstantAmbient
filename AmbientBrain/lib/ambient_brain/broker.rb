require "json"

class Broker
	
	attr_reader :sections

	def initialize(&block)
		@sections = []		
		instance_eval &block if block		
	end

	def section(name, &block)
		@sections << Section.new(name)
		block.call(self) if block_given?		
		self
	end

	def to(receiver, options)
		@sections[-1].add_receiver(receiver, options)		
	end

	def apply_data(data)
		begin
			configuration = JSON.parse(data)
		rescue JSON::ParserError
			return false
		end
		true
	end
	
	def to_s
		@sections.each do |s|
			puts s
		end
	end
	
end