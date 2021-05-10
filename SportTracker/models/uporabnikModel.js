var mongoose = require('mongoose');
var Schema   = mongoose.Schema;

var uporabnikSchema = new Schema({
	'username' : String,
	'starost' : Number,
	'teza' : Number,
	'visina' : Number
});

module.exports = mongoose.model('uporabnik', uporabnikSchema);
