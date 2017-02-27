var kafka = require('kafka-node'),
    Producer = kafka.Producer,
    KeyedMessage = kafka.KeyedMessage,
    client = new kafka.Client(),
    producer = new Producer(client),
    km = new KeyedMessage('key', 'message'),


    payloadMessage = process.argv.slice(2).join(" ");


//TOPIC: simpleTopic
payloads = [
    { topic: 'simpleTopic', messages: JSON.stringify({payload: payloadMessage }), partition: 0 }
];


producer.on('ready', function () {
    producer.send(payloads, function (err, data) {
        console.log("received data in topic, partition and offset" + JSON.stringify(data));
    });
});

producer.on('error', function (err) {
   console.log(err);
});

console.log("done!");
