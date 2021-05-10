var express = require('express');
var router = express.Router();
var uporabnikController = require('../controllers/uporabnikController.js');

/*
 * GET
 */
router.get('/', uporabnikController.list);

/*
 * GET
 */
router.get('/:id', uporabnikController.show);

/*
 * POST
 */
router.post('/', uporabnikController.create);

/*
 * PUT
 */
router.put('/:id', uporabnikController.update);

/*
 * DELETE
 */
router.delete('/:id', uporabnikController.remove);

module.exports = router;
