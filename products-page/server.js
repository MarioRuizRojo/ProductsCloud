const express = require('express')
const app = express()

app.use(express.static('build'))

app.listen(80, () => {
    console.log('Serving frontend app on port 80')
})