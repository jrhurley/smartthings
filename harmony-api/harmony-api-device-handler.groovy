/**
 *  Harmony API - Device Handler
 *
 *  Version 1.1
 *   - 1.0 Initial version
 *   - 1.1 Basic functionality incorporated - version bump
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
	definition (name: "Harmony API - Device Handler", namespace: "jrhurley", author: "Jonathon Hurley") {
    	command "sendCommand"
        
        capability "Refresh"
	}


	simulator {
		// TODO: define status and reply messages here
	}

	tiles(scale: 2) {
		standardTile("statusTile", "device.status", width: 6, height: 4) {
    		//state "online", label:'${name}', icon:"st.harmony.harmony-hub-icon", backgroundColor:"#009900"
    		//state "offline", label:'${name}', icon:"st.harmony.harmony-hub-icon", backgroundColor:"#AA0000"
            state "installed", label:'${name}', icon:"st.harmony.harmony-hub-icon", backgroundColor:"#AAAAAA"
		}
        
        //standardTile("refreshTile", "device.switch", width: 2, height: 2, decoration: "flat") {
		//	state "icon", action:"refresh.refresh", icon:"st.secondary.refresh", defaultState: true
		//}
	}
    
    main("statusTile")
    details(["statusTile","refreshTile"])
}

// parse events into attributes
def parse(String description) {
	log.debug "Parsing '${description}'"

}

// Refresh status
def refresh() {
	log.debug("Refresh called")

	//checkStatus()
}

/*
// checkStatus
// Check whether Harmony API server is reachable
def checkStatus() {
	def harmonyapiServer = getHostAddress()
    
	try {
    	sendHubCommand(new physicalgraph.device.HubAction("""GET /_ping HTTP/1.1\r\nHOST: $harmonyapiServer\r\nContent-Length:0\r\n\r\n""", physicalgraph.device.Protocol.LAN, null, [callback: checkStatusCallback]))
        log.debug("Hub command sent")
	}
	catch (Exception e) {
		log.debug("Exception $e when trying sendHubCommand")
    }
}

void checkStatusCallback(physicalgraph.device.HubResponse hubResponse) {
	if (hubResponse.status == 200) {
    	sendEvent(name: "status", value: "online")
    } else {
    	sendEvent(name: "status", value: "offline")
    }
    
	log.debug("checkStatusCallback $hubResponse.status -- $hubResponse.headers")
    
    runIn(3 * 60, refresh)
}
*/

// sendCommand
// Send a command to the Harmony API server
def sendCommand(cmd) {
	log.debug "Executing sendCommand"
    
    // Settings for harmonyapi server
	def harmonyapiServer = getHostAddress()
    
    def parsedCommand = parseCommand(cmd)
    log.debug("Parsed command: $parsedCommand")
    
    def hub = parsedCommand[0]
    def device = parsedCommand[1]
    def command = parsedCommand[2]
    
    if (hub == "" || hub == null || device == "" || device == null || command == "" || command == null) {
    	log.debug("Error in command")
        return
    }
    
    try {
    	sendHubCommand(new physicalgraph.device.HubAction("""POST /hubs/$hub/devices/$device/commands/$command HTTP/1.1\r\nHOST: $harmonyapiServer\r\nContent-Length:0\r\n\r\n""", physicalgraph.device.Protocol.LAN, null, [callback: callbackHandler]))
        log.debug("Hub command sent")
	}
	catch (Exception e) {
		log.debug("Exception $e when trying sendHubCommand")
    }
}

void callbackHandler(physicalgraph.device.HubResponse hubResponse) {
	log.debug("callbackHandler $hubResponse.status -- $hubResponse.headers")
}

def parseCommand(command) {
	return command.tokenize("/")
}



// gets the address of the device
private getHostAddress() {
    def ip = getDataValue("ip")
    def port = getDataValue("port")

    if (!ip || !port) {
        def parts = device.deviceNetworkId.split(":")
        if (parts.length == 2) {
            ip = parts[0]
            port = parts[1]
        } else {
            log.warn "Can't figure out ip and port for device: ${device.id}"
        }
    }

    log.debug "Using IP: $ip and port: $port for device: ${device.id}"
    return convertHexToIP(ip) + ":" + convertHexToInt(port)
}

private Integer convertHexToInt(hex) {
    return Integer.parseInt(hex,16)
}

private String convertHexToIP(hex) {
    return [convertHexToInt(hex[0..1]),convertHexToInt(hex[2..3]),convertHexToInt(hex[4..5]),convertHexToInt(hex[6..7])].join(".")
}