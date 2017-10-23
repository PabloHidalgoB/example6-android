# Aplicación de ejemplo 6 - Android

Esta aplicación se basa en el uso de servicios web para rescatar datos de tipo JSON entregado en una URL determinada para hacer uso
extensivo de esta, en este caso, un conversor de divisas.
Si bien esta aplicación no presenta múltiple activities, existen varios elementos que influyen directamente sobre esta, adapters,
custom classes, interfaces y cliente de WebService

<img src="https://github.com/PabloHidalgoB/example6-android/blob/master/app/src/main/res/screenshots/main.png" data-canonical-src="https://github.com/PabloHidalgoB/example6-android/blob/master/app/src/main/res/screenshots/main.png" width="200" height="auto" />

Esta sera la pantalla que se apreciara en primera instancia cuando el usuario entra en la aplicación, anteriormente a este punto, el
programa rescata los datos JSON desde la siguiente url: http://www.floatrates.com/daily/ils.json retornando un objeto JSON que es separado
en los distintos atributos que posee, estos datos son posteriormente guardados dentro de un arreglo de una clase custom, de el cual se
obtienen 3 datos importantes que son utilizados en su mayoría, el código de la moneda utilizada, la taza de cambio y el nombre de la
moneda, estos datos son separados y desplegados en los spinners que indican el nombre de moneda que entregamos y el nombre de moneda que
queremos obtener, por default, la moneda seleccionada que entregamos es la moneda chilena.

Cabe destacar que el link entregado es actualizado cada hora para mantenerse al día con los nuevos valores que todos los tipos de monedas
poseen, por lo cual este JSON es completamente dinamico

<img src="https://github.com/PabloHidalgoB/example6-android/blob/master/app/src/main/res/screenshots/switch.png" data-canonical-src="https://github.com/PabloHidalgoB/example6-android/blob/master/app/src/main/res/screenshots/switch.png" width="200" height="auto" />

El botón "Cambiar valores" realiza el cambio de las monedas seleccionadas sin interferir en el monto escrito.

<img src="https://github.com/PabloHidalgoB/example6-android/blob/master/app/src/main/res/screenshots/spinner.png" data-canonical-src="https://github.com/PabloHidalgoB/example6-android/blob/master/app/src/main/res/screenshots/spinner.png" width="200" height="auto" />

Si el usuario desea utilizar otro tipo de monedas, puede hacerlo a través de los dos spinners donde se entrega el nombre de moneda para su
selección

<img src="https://github.com/PabloHidalgoB/example6-android/blob/master/app/src/main/res/screenshots/valueInput.png" data-canonical-src="https://github.com/PabloHidalgoB/example6-android/blob/master/app/src/main/res/screenshots/valueInput.png" width="200" height="auto" />

Ya una vez seleccionadas las monedas de las cuales se quiere realizar el cambio, el usuario debe entregar el valor que desea cambiar
hasta un máximo de 15 cifras, tanto el botón con el visto bueno en el teclado numérico, como el botón "Convertir" son capaces de
realizar la operación esperada, entregando el resultado (redondeado a 2 décimas) en el centro de la pantalla como se puede apreciar en
la siguiente imagen

<img src="https://github.com/PabloHidalgoB/example6-android/blob/master/app/src/main/res/screenshots/valueOutput.png" data-canonical-src="https://github.com/PabloHidalgoB/example6-android/blob/master/app/src/main/res/screenshots/valueOutput.png" width="200" height="auto" />

Habrá notado que la aplicación despliega unos valores predeterminados desde la mitad de la pantalla, estos sirven para entregar
una vista rápida del valor (expresado en pesos Chilenos) de las siguientes divisas: Dolar Americano, Euro, Libras Esterlinas, Yen
Japones, Dolar Canadiense y el Zloty Polaco, con el fin de entregar una vista rápida de los valores mencionados sin tener la necesidad
de realizar la conversión de manera manual.
