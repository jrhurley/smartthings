# SmartThings for Harmony API

[Harmony API](https://github.com/maddox/harmony-api) by Jon Maddox is a fantastic nodejs app which allows you to control a [Logitech Harmony Hub](https://www.logitech.com/en-gb/product/harmony-hub) via a REST interface.
	
It allows control of not only Harmony Hub activities, but also individual devices outside of activites, which gives amazing power to control the devices in your home through SmartThings.  By using appropriate switch types to control these devices, you can link them to Amazon's Echo and Echo Dot with Alexa and control them by voice.
	
This is in contrast to using the Logitech Harmony skill for Alexa - although this lets you control activities, it restricts you to specific sets of commands.  Changing between activities causes devices to turn off unintentionally; there isn't an easy way to make activities independent of each other.
	
## Prerequisities

To use Harmony API with SmartThings, you will need:

1. A working installation of Harmony API on nodejs - I'd suggest my fork of harmony-api which includes SSDP service discovery to remove the need to hard-wire the IP address of the Harmony API server into SmartThings.  See [st-harmonyapi-ssdp](https://github.com/jrhurley/st-harmonyapi-ssdp).
2. The Harmony API SmartThings from this repository:
 * *harmony-api-service-manager.groovy* - the service manager SmartApp which is responsible for finding Harmony API servers on your network (when paired with the SSDP fork of Harmony API) and managing servers as devices within SmartThings
 * *harmony-api-device-handler.groovy* - the Harmony API server device handler; this manages the sending of commands to the server
3. SmartApps to link switches and buttons to the Harmony API server
 * *harmony-api-switch.groovy* - similar to a typical on / off switch, this defines two sets of commands to be sent to the Harmony API server: one set when the switch is turned on and one set when the switch is turned off.  This links to any SmartThings device with the _switch_ capability.
 * *harmony-api-momentary.groovy* - this acts as a momentary pushbutton, sending a single set of commands to the Harmony API server when activated.  This is used with a SmartThings device having the _momentary_ capability.
 
Optionally, *repeating-on-off-switch.groovy* provides an on / off switch which will repeat the on or off command.  The built-in switches in SmartThings will only issue an on or off command once, even if activated multiple times.  This can cause problems when a device controlled via Harmony API is manually switched on or off.  This can lead to a state where SmartThings thinks a device is on, but it is actually off - and with the built-in switch type you cannot switch the device on again.  This repeating on / off switch will allow you to issue the on and off commands multiple times to get the SmartThings switch and device back in sync.
 
## Installing the Service Manager

The Service Manager allows SmartThings to discover Harmony API servers on the network and maintains information about their IP addresses.  To use the version of the Service Manager in this repository, you will need a Harmony API server which implements SSDP (Simple Service Discovery Protocol - part of the UPnP suite).  I have forked Jon Maddox's original Harmony API server and added SSDP - you can find out more details, including installation details, [here](https://github.com/jrhurley/st-harmonyapi-ssdp).

To install the Service Manager, you will need to log in to the SmartThings development site at [https://graph.api.smartthings.com](https://graph.api.smartthings.com).  Click the _Log In_ link and log in to the site with your SmartThings account.

SmartThings accounts are based on geographically-appropriate servers depending on your own country.  You need to log in to the developer site on the appropriate server to be able to add SmartApps and Device Handlers.  To do this, go to  _My Locations_ in the top menu of the SmartThings developer site.  Click on the name of the location where you have your SmartThings hub set up and you will be directed to log in to another site - for me, this is [https://graph-eu01-euwest1.api.smartthings.com](https://graph-eu01-euwest1.api.smartthings.com).  Log in again with your SmartThings account and you will now be able to create SmartApps.

The Service Manager is a SmartApp, so click on _My SmartApps_ in the top menu.  Click on _+ New SmartApp_ and choose the _From Code_ tab.  Copy and paste the code from [https://github.com/jrhurley/smartthings/blob/master/harmony-api/harmony-api-service-manager.groovy](https://github.com/jrhurley/smartthings/blob/master/harmony-api/harmony-api-service-manager.groovy) into this and click _Create_ - the Service Manager SmartApp will now be created.  To be able to use this in SmartThings, click _Publish - For Me_.

To discover Harmony API servers and install them in SmartThings, open the SmartThings app on your device and go to _SmartApps_ under _Automation_.  Choose _+ Add a SmartApp_ and then open _My Apps_ at the end of the list.  Here you will find all of your custom SmartApps.

Before running the Service Manager, however, you will need to install the Device Handler which will manage individual servers.

## Installing the Device Handler

The Device Handler manages individual Harmony API servers and must be available in your SmartApps (i.e. published for you) before you run the Service Manager.

Installation of the Device Handler is very similar to installing the Service Manager.  The instructions in the previous section apply, except for the following differences:

* Instead of creating a SmartApp under _My SmartApps_, you need to click on _My Device Handlers_ and then _+ Create a New Device Handler_.
* Copy and paste the code from [https://github.com/jrhurley/smartthings/blob/master/harmony-api/harmony-api-device-handler.groovy](https://github.com/jrhurley/smartthings/blob/master/harmony-api/harmony-api-device-handler.groovy) into the _From Code_ tab of the Device Handler creation screen.
* Remember to publish the Device Handler in order to be able to use it in your SmartApps.

## Using the Service Manager and Device Handler

Once you have installed both the Service Manager and Device Handler, you can run the Service Manager app to discover Harmony API servers.  You can do this through _SmartApps_ under _Automation_ by adding the Service Handler SmartApp.  When you do this you will see a list where you can select Harmony API servers to add to your device list.  You will then be able to use these in other SmartApps to control devices.

## Installing the Switch and Momentary SmartApps

In order to send commands to a Harmony API server, you will need to use either the Harmony API Switch or Harmony API Momentary SmartApps to link a switch or momentary input to a set of commands.

Install the switch [https://github.com/jrhurley/smartthings/blob/master/harmony-api/harmony-api-switch.groovy](https://github.com/jrhurley/smartthings/blob/master/harmony-api/harmony-api-switch.groovy) or momentary [https://github.com/jrhurley/smartthings/blob/master/harmony-api/harmony-api-momentary.groovy](https://github.com/jrhurley/smartthings/blob/master/harmony-api/harmony-api-momentary.groovy) SmartApps in the same way as you installed the Service Manager SmartApp. Remember to publish them afterwards.

Add the SmartApps through _Automation_ - _SmartApps_ - _+ Add a SmartApp_.  They will appear under _My Apps_.  When you choose a SmartApp to add, you will be able to chose the switch that you want to use to activate the SmartApp as well as the Harmony API server to use to send commands to.  You can also assign a name to the SmartApp - this should reflect the function of the commands so that you know which app relates to which device.

On the next page, you can enter up to five commands to send to the device.  For a switch, there is a page for 'on' commands and a page for 'off' commands which respond to the switch state changing.  For a momentary switch, there is a single page in which to enter a set of commands to send when the switch is 'pushed'.

Commands are in the form of _hub\_slug_/_device\_slug_/_command\_slug_.  See the Harmony API documentation at [https://github.com/maddox/harmony-api](https://github.com/maddox/harmony-api) for more details on these definitions.  A typical command will look like _harmomy\_hub/TV/power\_on_.

You can enter up to five commands to be sent in order - this can be used to turn on multiple devices with a single switch, for example a TV with its soundbar or a games console, TV and surround sound system.

## Using the Repeating On / Off Switch

Using the built-in switch in SmartApps works well if that is the only way that you will control a device through Harmony API.  However, it only sends each 'on' or 'off' state once.  If you turn a device on with the SmartApp, and then turn it off with its remote control, you will not be able to reissue the 'on' command to turn the device back on and you will have to resynchronise the device and the SmartApp switch status using the device's remote control.

The Repeating On / Off Switch gets around the limitation of only being able to send each 'on' or 'off' state once.  It has the same capability as the built-in switch, and can be used with the Harmony API SmartApps in this repository, but you can issue the 'on' and 'off' commands multiple times.  

Install the Repeating On / Off Switch SmartApp in the same way as the other SmartApps in this repository, remembering to publish it.

To add a Repeating On / Off switch to your list of devices, you will need to go to _My Devices_ on the developer website and then choose _+ New Device_.  Enter a name for the switch and a unique string as the Device Network ID (this must be unique).  Look for _Repeating On / Off Switch_ at the end of the _Type_ dropdown list, and assign the switch to a _Location_ and _Hub_. 

You will then be able to use the switch from your SmartThings app and link it to the SmartApps in this respository or other SmartApps available.

The Repeating On / Off Switch is particularly useful when switches are controlled by voice through Amazon's Alexa service as it is easy to forget to use Alexa and turn a device off by remote control.

## Contributions

Feel free to fork this repository and develop your own Harmony API SmartApps.  If you base anything on my scripts here, please let me know and I'll add a link to your repository.

