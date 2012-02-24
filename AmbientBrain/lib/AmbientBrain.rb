require "AmbientBrain/version"
require "amqp"
require "json"


#AmbientBrain::start
EventMachine.run do
			  connection = AMQP.connect(:host => '127.0.0.1', :port => 3000)
			  puts "Connecting to AMQP broker. Running #{AMQP::VERSION} version of the gem..."

			  channel  = AMQP::Channel.new(connection)
			  queue    = channel.queue("amqpgem.examples.hello_world", :auto_delete => true)
			  exchange = channel.default_exchange

			  queue.subscribe do |payload|
			    puts "Received a message: #{payload}. Disconnecting..."

			    connection.close {
			      EventMachine.stop { exit }
			    }
			  end

			  exchange.publish "Hello, world!", :routing_key => queue.name
end
