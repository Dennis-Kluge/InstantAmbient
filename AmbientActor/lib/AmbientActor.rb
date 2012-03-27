require "AmbientActor/version"
require "eventmachine"

class ActorConnection < EventMachine::Connection
   
  def receive_data(data)
    p data

    #@broker.apply_data(data)

    #send_data data
    #close_connection_after_writing 
  end

  def unbind
    p ' connection closed'
  end

end

EventMachine.run {
  EventMachine::start_server('127.0.0.1', 9123, ActorConnection)
  puts "Starting actor..."
}
