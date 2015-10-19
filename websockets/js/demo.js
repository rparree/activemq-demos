var STOMP = (function () {
    'use strict';

    /**
     * Creates the Demo Stomp API
     * @param {String} url the WebSocket url
     * @constructor
     */
    function DemoClient(url) {
        this.url = url;
        this.socket = null;
        this.messageCallback = null;
    }

    /**
     * Connect to the web socket and to the broker
     * (sends the CONNECT frame)
     * @param {function} callback function called when connected to the broker
     */
    DemoClient.prototype.connect = function (callback) {
        var _this = this;
        this.socket = new WebSocket(this.url);

        this.socket.onopen = function () {
            //callback()
            window.console.log("WebSocket is open");

            _this.socket.send(_this._marshall("CONNECT", {
                "accept-version": "1.2",
                host: "localhost",
                login: "admin",
                passcode: "admin"
            }));
        };

        this.socket.onerror = function (error) {
            window.console.log('WebSocket Error ' + error);
        };

        this.socket.onmessage = function (e) {
            window.console.log(e);
            if (e.data.indexOf("CONNECTED") === 0) {
                if (typeof callback === "function") {
                    callback();
                }
            }
            if (typeof _this.messageCallback === "function") {
                if (e.data.indexOf("MESSAGE") === 0) {
                    _this.messageCallback(e.data);

                }
            }
        };

    };

    /**
     * Disconnect from the broker
     * (sends the DISCONNECT frame)
     * @param {function} [callback] called when disconnected
     */
    DemoClient.prototype.disconnect = function (callback) {
        if (this.socket === null) {
            return;
        }
        this.socket.send(this._marshall("DISCONNECT"));


        if (typeof callback === "function") {
            callback();
        }

    };

    /**
     * Subscribe to a destination
     * (sends the SUBSCRIBE frame)
     * @param {String} destination to physical name of the destination (topic or queue)
     * @param {function} [callback] function called when messages arrive
     */
    DemoClient.prototype.subscribe = function (destination, callback) {
        window.console.log("subscribing");
        if (this.socket === null) {
            return;
        }
        this.socket.send(this._marshall("SUBSCRIBE", {
            id: "1",
            destination: destination,
            ack: "auto"
        }));
        this.messageCallback = callback;

    };

    /**
     * Helper method to marshall a STOMP request
     * @param {String} command The command (e.g, CONNECT, ACK, DISCONNECT etc.)
     * @param {Object} [header] Header values to be send as part of the frame
     * @param {String} [body] Body to send
     * @returns {string} The marshaled STOMP request
     * @private
     */
    DemoClient.prototype._marshall = function (command, header, body) {

        var frame = [command];
        if (header !== undefined) {
            for (var name in header) {
                if (header.hasOwnProperty(name)) {
                    frame.push("" + name + ":" + header[name]);
                }
            }
        }
        if (command !== "CONNECT") {
            frame.push("receipt:");
        }

        frame.push('\n');
        if (body !== undefined) {
            frame.push(body);
        }
        frame.push('\x00');
        return frame.join('\n');
    };

    return {DemoClient: DemoClient}; // export from namespace
}());