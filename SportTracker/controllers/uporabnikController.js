var UporabnikModel = require('../models/uporabnikModel.js');

/**
 * uporabnikController.js
 *
 * @description :: Server-side logic for managing uporabniks.
 */
module.exports = {

    /**
     * uporabnikController.list()
     */
    list: function (req, res) {
        UporabnikModel.find(function (err, uporabniks) {
            if (err) {
                return res.status(500).json({
                    message: 'Error when getting uporabnik.',
                    error: err
                });
            }

            return res.json(uporabniks);
        });
    },

    /**
     * uporabnikController.show()
     */
    show: function (req, res) {
        var id = req.params.id;

        UporabnikModel.findOne({_id: id}, function (err, uporabnik) {
            if (err) {
                return res.status(500).json({
                    message: 'Error when getting uporabnik.',
                    error: err
                });
            }

            if (!uporabnik) {
                return res.status(404).json({
                    message: 'No such uporabnik'
                });
            }

            return res.json(uporabnik);
        });
    },

    /**
     * uporabnikController.create()
     */
    create: function (req, res) {
        var uporabnik = new UporabnikModel({
			username : req.body.username,
			starost : req.body.starost,
			teza : req.body.teza,
			visina : req.body.visina
        });

        uporabnik.save(function (err, uporabnik) {
            if (err) {
                return res.status(500).json({
                    message: 'Error when creating uporabnik',
                    error: err
                });
            }

            return res.status(201).json(uporabnik);
        });
    },

    /**
     * uporabnikController.update()
     */
    update: function (req, res) {
        var id = req.params.id;

        UporabnikModel.findOne({_id: id}, function (err, uporabnik) {
            if (err) {
                return res.status(500).json({
                    message: 'Error when getting uporabnik',
                    error: err
                });
            }

            if (!uporabnik) {
                return res.status(404).json({
                    message: 'No such uporabnik'
                });
            }

            uporabnik.username = req.body.username ? req.body.username : uporabnik.username;
			uporabnik.starost = req.body.starost ? req.body.starost : uporabnik.starost;
			uporabnik.teza = req.body.teza ? req.body.teza : uporabnik.teza;
			uporabnik.visina = req.body.visina ? req.body.visina : uporabnik.visina;
			
            uporabnik.save(function (err, uporabnik) {
                if (err) {
                    return res.status(500).json({
                        message: 'Error when updating uporabnik.',
                        error: err
                    });
                }

                return res.json(uporabnik);
            });
        });
    },

    /**
     * uporabnikController.remove()
     */
    remove: function (req, res) {
        var id = req.params.id;

        UporabnikModel.findByIdAndRemove(id, function (err, uporabnik) {
            if (err) {
                return res.status(500).json({
                    message: 'Error when deleting the uporabnik.',
                    error: err
                });
            }

            return res.status(204).json();
        });
    }
};
