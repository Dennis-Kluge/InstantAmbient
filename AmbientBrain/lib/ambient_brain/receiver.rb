require "xmlsimple"
require "json"
require "socket"
require "ambient_brain/converter"

class Receiver

	attr_reader :format, :secure, :name

	def initialize(name, options)		
		@name = name		
		@secure = false
		
		options.each do |key, value|			
			case key
			when :format
				@format = value
			when :secure				
				@secure = value
			when :address
				@address = value
			when :port
				@port = value
			end						
		end		
	end	

	def send(value)
		puts "#{name}-SEND: #{value}"
		convert(value)
		socket = TCPSocket.open(@address, @port)
		socket.write(convert(value))
		socket.flush
	end

	def convert(data)		
		case format
		when :json
			converted_data = Converter.to_json(data)
		when :xml			
			converted_data = Converter.to_xml(data)
		when :txt
			converted_data = Converter.to_txt(data)
		else	
			converted_data = data
		end
		converted_data
	end

	def to_s
		"	-> to #{@name} with format #{@format} and secure = #{@secure}"		
	end	
end