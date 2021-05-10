var mongoose = require('mongoose');
var Schema   = mongoose.Schema;

var tekSchema = new Schema({
	'trajanje' : Number,
	'razdalja' : Number,
	'hitrost' : Number,
	'datum' : Date,
	'lokacija' : Array,
	'utrip' : Number,
	'kalorije' : Number
});

module.exports = mongoose.model('tek', tekSchema);
