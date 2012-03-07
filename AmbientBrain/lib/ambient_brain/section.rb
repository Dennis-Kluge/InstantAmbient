class Section

	attr_reader :receivers

	def initialize(name)
		@name = name
		@receivers = []
	end
	
	def add_receiver(receiver, options={})		
		@receivers << Receiver.new(receiver, options)
	end

	def to_s
		puts "Section #{@namye}"
		@receivers.each do |r|
			puts r
		end
		puts "\n"
	end
end