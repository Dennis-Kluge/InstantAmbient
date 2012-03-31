require "json"

class Broker
	
	attr_reader :sections, :receivers

	def initialize(&block)
		@sections = {}
		@receivers = []		
		@last_section = :none
		instance_eval &block if block		
	end

	def section(name, &block)
		@sections[name] = Array.new
		@last_section = name		
		block.call(self) if block_given?				
	end

	def receiver(name, options)		
		@receivers << Receiver.new(name, options)				
	end

	def to(receiver)
		@sections[@last_section] << find_receiver(receiver)		
	end

	def apply_data(data)
		begin
			configuration = JSON.parse(data)
		rescue JSON::ParserError
			return false
		end
		process_configuration(configuration)

		true
	end
	
	def process_configuration(configuration)
		configuration.each do |key, value|										
			# can be processed 
			if @sections[key.to_sym]
				# send value to each receiver
				@sections[key.to_sym].each do |receiver|
					receiver.send(value)
				end
			end
		end		
	end

	def find_receiver(name)
		@receivers.each do |s|
			if s.name == name
				return s				
			end
		end	
	end

	def to_s
		@sections.each do |s|
			puts s
		end
	end
	
end