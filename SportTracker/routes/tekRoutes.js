var express = require('express');
var router = express.Router();
var tekController = require('../controllers/tekController.js');

/*
 * GET
 */
router.get('/', tekController.list);

/*
 * GET
 */
router.get('/:id', tekController.show);

/*
 * POST
 */
router.post('/', tekController.create);

/*
 * PUT
 */
router.put('/:id', tekController.update);

/*
 * DELETE
 */
router.delete('/:id', tekController.remove);

module.exports = router;
