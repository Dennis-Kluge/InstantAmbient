require "AmbientConnector/version"
require "java"
require "../jar/bluecove-2.1.1.jar"
require "json"
require "socket"
require "optparse"


module AmbientConnector

	import "javax.bluetooth.UUID"
	import "javax.bluetooth.RemoteDevice"
	import "javax.microedition.io.StreamConnectionNotifier"
	import "javax.microedition.io.Connector"
	import "java.io.BufferedReader"
	import "java.io.IOException"
	import "java.io.InputStream"
	import "java.io.InputStreamReader"
	import "java.io.OutputStream"
	import "java.io.OutputStreamWriter"
	import "java.io.PrintWriter"
	
	class SPPServer

		def initialize(options)												
			puts "Connecting to brain at #{options[:host]} on port #{options[:port]}"
			@socket = TCPSocket.open(options[:host], options[:port])			
		end
		
		def start_server
			uuid = UUID.new("1101", true)
			connection_string = "btspp://localhost:#{uuid};name=AmbientConnector";			
			loop do
				
				connection_notifier = Connector.open(connection_string)
				#start connection
				connection = connection_notifier.accept_and_open();
				input_stream = connection.open_input_stream
				output_stream= connection.open_output_stream();
				puts "Connection opened"

				# get device info
				device = RemoteDevice.get_remote_device(connection)			
				puts "Remote device address: #{device.get_bluetooth_address()}"
	      puts "Remote device name:    #{device.get_friendly_name(true)}"
	      
	      buffered_reader = BufferedReader.new(InputStreamReader.new(input_stream))      
	      configuration = buffered_reader.read_line
	      	    
	      #send response	      
	      print_writer= PrintWriter.new(OutputStreamWriter.new(output_stream))	      
	      if handle_configuration(configuration)
	      	print_writer.write("ACCEPT")
	      else
	      	print_writer.write("ERROR")
	      end
	      print_writer.write("ACCEPT")
	      print_writer.flush	 			
	 			print_writer.close
	     	connection_notifier.close		      	      
	     end	     
		end
		
		def handle_configuration(configuration)
			#is configuration valid?			
			begin 
				valid_configuration = JSON.parse(configuration)
			rescue
				return false
			end
			# add connector information to JSON
			valid_configuration[:connector] = "AmbientConnector"
			json_configuration = JSON.generate(valid_configuration)
			puts json_configuration
			send_configuration(json_configuration)
			true
		end

		def send_configuration(configuration)						
			puts "Sending configuration to brain"
			@socket.write(configuration)
			@socket.flush
		end
	end
end

# options = {:host => "localhost", :port => 8081}
# OptionParser.new do |opts|
#   opts.banner = "Usage: AmbienConnector.rb -h localhost -p 8081"

# 	opts.on("-h", "--host HOST", "address to brain", String) do |h|
#     options[:host] = h
#   end

#   opts.on("-p", "--port PORT", "port to brain", Integer) do |p|
#     options[:port] = p
#   end
# end.parse!

# server = AmbientConnector::SPPServer.new(options)
# server.start_server
