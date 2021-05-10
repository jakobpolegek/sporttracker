var TekModel = require('../models/tekModel.js');

/**
 * tekController.js
 *
 * @description :: Server-side logic for managing teks.
 */
module.exports = {

    /**
     * tekController.list()
     */
    list: function (req, res) {
        TekModel.find(function (err, teks) {
            if (err) {
                return res.status(500).json({
                    message: 'Error when getting tek.',
                    error: err
                });
            }

            return res.json(teks);
        });
    },

    /**
     * tekController.show()
     */
    show: function (req, res) {
        var id = req.params.id;

        TekModel.findOne({_id: id}, function (err, tek) {
            if (err) {
                return res.status(500).json({
                    message: 'Error when getting tek.',
                    error: err
                });
            }

            if (!tek) {
                return res.status(404).json({
                    message: 'No such tek'
                });
            }

            return res.json(tek);
        });
    },

    /**
     * tekController.create()
     */
    create: function (req, res) {
        var tek = new TekModel({
			trajanje : req.body.trajanje,
			razdalja : req.body.razdalja,
			hitrost : req.body.hitrost,
			datum : req.body.datum,
			lokacija : req.body.lokacija,
			utrip : req.body.utrip,
			kalorije : req.body.kalorije
        });

        tek.save(function (err, tek) {
            if (err) {
                return res.status(500).json({
                    message: 'Error when creating tek',
                    error: err
                });
            }

            return res.status(201).json(tek);
        });
    },

    /**
     * tekController.update()
     */
    update: function (req, res) {
        var id = req.params.id;

        TekModel.findOne({_id: id}, function (err, tek) {
            if (err) {
                return res.status(500).json({
                    message: 'Error when getting tek',
                    error: err
                });
            }

            if (!tek) {
                return res.status(404).json({
                    message: 'No such tek'
                });
            }

            tek.trajanje = req.body.trajanje ? req.body.trajanje : tek.trajanje;
			tek.razdalja = req.body.razdalja ? req.body.razdalja : tek.razdalja;
			tek.hitrost = req.body.hitrost ? req.body.hitrost : tek.hitrost;
			tek.datum = req.body.datum ? req.body.datum : tek.datum;
			tek.lokacija = req.body.lokacija ? req.body.lokacija : tek.lokacija;
			tek.utrip = req.body.utrip ? req.body.utrip : tek.utrip;
			tek.kalorije = req.body.kalorije ? req.body.kalorije : tek.kalorije;
			
            tek.save(function (err, tek) {
                if (err) {
                    return res.status(500).json({
                        message: 'Error when updating tek.',
                        error: err
                    });
                }

                return res.json(tek);
            });
        });
    },

    /**
     * tekController.remove()
     */
    remove: function (req, res) {
        var id = req.params.id;

        TekModel.findByIdAndRemove(id, function (err, tek) {
            if (err) {
                return res.status(500).json({
                    message: 'Error when deleting the tek.',
                    error: err
                });
            }

            return res.status(204).json();
        });
    }
};
