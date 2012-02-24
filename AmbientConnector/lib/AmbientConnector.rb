require "AmbientConnector/version"
require "java"
require "../jar/bluecove-2.1.1.jar"
require "json"


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
	      configuration = ""      
	      # __EOF__ is the termination symbol
	      while (line = buffered_reader.read_line) != "__EOF__"
	      	puts line
	      	unless line then break end      	      
	      	configuration += line       	       	      
	      end
	      
	      puts "Configuration: #{configuration}"
	      #send response	      
	      print_writer= PrintWriter.new(OutputStreamWriter.new(output_stream))
	      # if handle_configuration
	      # 	print_writer.write("ACCEPT")
	      # else
	      # 	print_writer.write("ERROR")
	      # end
	      print_writer.write("ACCEPT")
	      print_writer.flush	 			
	 			print_writer.close
	     	connection_notifier.close		      	      
	     end	     
		end

		private
		def handle_configuration(configuration)
			#is configuration valid?
			begin 
				valid_configuration = JSON.parse(configuration)
			rescue
				return false
			end
			# add connector information to JSON
			valid_configuration[:connector] = "AmbientConnector"
			json_configuration = valid_configuration.generate
			put json_configuration
		end

		def send_configuration
		end
	end
end

server = AmbientConnector::SPPServer.new
server.start_server