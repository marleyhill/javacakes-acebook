require 'rest-client'

def application_up_and_running?
    begin
        RestClient.get 'http://localhost:4567/'
        return true
    rescue Exception => e
        return false
    end
end

def start_application
    cmd = './start_application.sh'
    res = system(cmd)
    attempts_left = 30
    while attempts_left > 0
        up_and_running = application_up_and_running?
        return if up_and_running
        $stdout.puts "Waiting for the application... (attemps left #{attempts_left})"
        sleep(2)
        attempts_left = attempts_left - 1
    end
    stop_application
    raise Exception.new('The application does not respond, giving up')
end

def stop_application
    cmd = './stop_application.sh'
    res = system(cmd)
end