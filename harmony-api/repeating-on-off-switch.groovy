/**
 *  Repeating On / Off Switch
 *
 *  Simulate an on / off switch for which the on or off commands can be called multiple times
 *  This is usually prevented by comparing the current state of the switch to the requested method and ignoring the call if they are the same
 *  However this means that devices can become out of sync if they are manually switched off or on without using the SmartThings switch
 *  This is done by setting isStateEvent to true in the sendEvent call
 *
 *  Copyright 2017 Jonathon Hurley
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License. You may obtain a copy of the License at:
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 *  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
 *  for the specific language governing permissions and limitations under the License.
 *
 */
metadata {
	definition (name: "Repeating On  / Off Switch", namespace: "jrhurley", author: "Jonathon Hurley") {
		capability "Switch"
	}
    
    tiles(scale: 2) {
		// standard tile with actions
		standardTile("switchTile", "device.switch", width: 2, height: 2, canChangeIcon: true) {
			state "off", label: '${currentValue}', action: "switch.on", icon: "st.switches.switch.off", backgroundColor: "#ffffff"
			state "on", label: '${currentValue}', action: "switch.off", icon: "st.switches.switch.on", backgroundColor: "#00A0DC"
		}
        
        standardTile("switchOn", "device.switch", width: 2, height: 2, decoration: "flat") {
			state "switchOn", label:'on', action:"on", defaultState: true, backgroundColor: "#6EE822"
		}
		standardTile("switchOff", "device.switch", width: 2, height: 2, decoration: "flat") {
			state "switchOff", label:'off', action:"off", defaultState: true, backgroundColor: "#E82F22"
		}
    }
    
	simulator {
		// TODO: define status and reply messages here
	}
    
    main("switchTile")
    details(["switchTile","switchOn","switchOff"])
}

// parse events into attributes
def parse(String description) {
	log.debug "Parsing '${description}'"
	// TODO: handle 'switch' attribute

}

// handle commands
def on() {
	log.debug "Executing 'on'"
	sendEvent(name: "switch", value: "on", isStateChange: true)
}

def off() {
	log.debug "Executing 'off'"
	sendEvent(name: "switch", value: "off", isStateChange: true)
}