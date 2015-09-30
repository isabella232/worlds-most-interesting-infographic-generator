// page init
jQuery(function(){
	initSameHeight();
	initFriendsChart();
	initPostTypesChart();
	initBarChart();
	initLineBar();
	initParallaxBg();
	initFadeBlock();
});

// post privacy line chart
function initLineBar() {
	var holder = d3.select('#post-privacy-line-chart');
	if (!holder.node()) return;

	var width = 580;
	var height = 380;
	var bottomOffsetAxis = 60;
	var leftOffsetAxis = 70;
	var topOffset = 15;
	var offsetLeft = 0;
	var lineWidth = 3;
	var offsetLeftXAxis = 1;
	var ticksLenght = 25;
	var textTicksOffsetLeft = 30;
	var rotateValue = 7;

	var dataUrlPublic = holder.attr('data-public');
	var dataJSONPublic;

	var dataUrlPrivate = holder.attr('data-private');
	var dataJSONPrivate;

	d3.json(dataUrlPublic, function (error, json) {
		if (error) return console.warn(error);
		dataJSONPublic = json[0];

		d3.json(dataUrlPrivate, function (errorState, jsonPublic) {
			if (error) return console.warn(error);
			dataJSONPrivate = jsonPublic[0];

			// add main svg
			var svg = holder.append('svg')
				.attr('width', width + leftOffsetAxis)
				.attr('height', height + bottomOffsetAxis + topOffset)
				.attr('preserveAspectRatio', 'xMidYMid meet')
				.attr('viewBox', '0 0 ' + (width + leftOffsetAxis) + ' ' + (height + bottomOffsetAxis + topOffset));

			// add main group
			var mainGroup = svg.append('g')
				.attr('class', 'line-chart')
				.attr("transform", "translate(" + leftOffsetAxis + "," + topOffset + ")");

			//add scales
			var x = d3.scale.linear()
				.range([0, width]);

			var y = d3.scale.linear()
				.range([height, 0]);

			// comma numbers axis Y
			var axisYFormatters = d3.locale({
				  "decimal": ",",
				  "thousands": ".",
				  "grouping": [3],
				  "currency": ["$", ""],
				  "dateTime": "%a %b %e %X %Y",
				  "date": "%m/%d/%Y",
				  "time": "%H:%M:%S",
				  "periods": ["AM", "PM"],
				  "days": ["Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"],
				  "shortDays": ["Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"],
				  "months": ["January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"],
				  "shortMonths": ["Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"]
			});
			var numberFormat = axisYFormatters.numberFormat(",.1f");

			// add axes
			var xAxis = d3.svg.axis()
				.scale(x)
				.orient("top");

			var yAxis = d3.svg.axis()
				.scale(y)
				.orient("left")
				.tickFormat(numberFormat);

			// set line layout
			var line = d3.svg.line()
				.x(function(d) { return x(d.percent); })
				.y(function(d) { return y(d.value); });

			// set domain scales
			var maxXPrivate = d3.max(dataJSONPrivate.private, function (d) {return d.percent;});
			var maxXPubluc = d3.max(dataJSONPublic.public, function (d) {return d.percent;});

			var maxYPrivate = d3.max(dataJSONPrivate.private, function (d) {return d.value;});
			var maxYPubluc = d3.max(dataJSONPublic.public, function (d) {return d.value;});

			x.domain([0, d3.max([maxXPrivate, maxXPubluc])]);
			y.domain([0, 1.15 * d3.max([maxYPrivate, maxYPubluc])]);

			// add axes
			mainGroup.append("g")
				.attr("class", "y axis")
				.call(yAxis)
				.attr("transform", "translate(" + offsetLeft + "," + 0 + ")");;

			mainGroup.append("g")
				.attr("class", "x axis")
				.attr("transform", "translate(" + -offsetLeftXAxis + "," + (height + bottomOffsetAxis) + ")")
				.call(xAxis);

			// format axis X
			mainGroup
				.selectAll("g.x.axis text")
				.attr('transform', function(d) {
					return 'rotate(' + -90 + ' ' + -rotateValue + ' ' + 7 + ')';
				})
				.style('text-anchor', 'start')
				.text(function(d, i) {
					if (i === 0) {
						return '';
					} else {
						return d + '%';
					}
				});

			mainGroup
				.selectAll("g.x.axis line")
				.attr('y2', -ticksLenght);

			// format axis Y + lines
			mainGroup.selectAll("g.y.axis g.tick")
				.append("line")
				.classed("grid-line", true)
				.attr("x1", offsetLeft)
				.attr("y1", 0)
				.attr("x2", width)
				.attr("y2", 0)
				.style("stroke", '#aeadae');

			// format axis Y
			mainGroup
				.selectAll("g.y.axis text")
				.attr('dx', -textTicksOffsetLeft);

			// main chart lines
			mainGroup.append("path")
				.datum(dataJSONPrivate.private)
				.attr("class", "line-private")
				.attr("d", line)
				.attr("fill", 'none')
				.attr("stroke", function() {
					return dataJSONPrivate.color;
				})
				.attr("stroke-width", lineWidth);

			mainGroup.append("path")
				.datum(dataJSONPublic.public)
				.attr("class", "line-public")
				.attr("d", line)
				.attr("fill", 'none')
				.attr("stroke", function(d) {
					return dataJSONPublic.color;
				})
				.attr("stroke-width", lineWidth);

			// resize handler
			var chartHolder = svg.select('.line-chart');
			var ratio = chartHolder.node().getBoundingClientRect().width / chartHolder.node().getBoundingClientRect().height;
			d3.select(window)
				.on('resize.line-chart', function() {
					svg
						.attr('height', function() {
							return svg.node().getBoundingClientRect().width / ratio;
						});
				});

			svg
				.attr('height', function() {
					return svg.node().getBoundingClientRect().width / ratio;
				});
		});
	});
}

// post frequency bar chart
function initBarChart() {
	var holder = d3.select('#post-frequency-bar-chart');
	if (!holder.node()) return;

	var dataUrl = holder.attr('data-json');
	var dataJSON;
	var width = 600;
	var height = 380;
	var colorHighest = '#3a5897';
	var colorLowest = '#ef894a';
	var bottomOffsetAxis = 35;
	var leftOffsetAxis = 39;
	var topOffset = 15;
	var offsetLeft = 12;

	// set color output diapason
	var color = d3.scale.ordinal()
		.range([colorHighest, colorLowest]);

	d3.json(dataUrl, function (error, json) {
		if (error) return console.warn(error);
		dataJSON = json[0];

		// add main svg
		var svg = holder.append('svg')
			.attr('width', width + leftOffsetAxis)
			.attr('height', height + bottomOffsetAxis + topOffset)
			.attr('preserveAspectRatio', 'xMidYMid meet')
			.attr('viewBox', '0 0 ' + (width + leftOffsetAxis) + ' ' + (height + bottomOffsetAxis + topOffset));

		// crate scales
		var xScale = d3.scale.ordinal()
			.rangeRoundBands([0, width], 0.5)
			.domain(dataJSON.frequency.map(function(d) { return d.month;}));

		var yScale = d3.scale.linear()
			.rangeRound([height, 0]);

		// comma numbers axis Y
		var axisYFormatters = d3.locale({
			  "decimal": ",",
			  "thousands": ".",
			  "grouping": [3],
			  "currency": ["$", ""],
			  "dateTime": "%a %b %e %X %Y",
			  "date": "%m/%d/%Y",
			  "time": "%H:%M:%S",
			  "periods": ["AM", "PM"],
			  "days": ["Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"],
			  "shortDays": ["Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"],
			  "months": ["January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"],
			  "shortMonths": ["Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"]
		});
		var numberFormat = axisYFormatters.numberFormat(",.1f"); 

		// crate axes
		var xAxis = d3.svg.axis()
			.scale(xScale)
			.orient("bottom");

		var yAxis = d3.svg.axis()
			.scale(yScale)
			.orient("left")
			.tickFormat(numberFormat);

		// set color entries data
		 color.domain(d3.keys(dataJSON.frequency[0]).filter(function(key) { return key !== "month"; }));

		// data transormation
		dataJSON.frequency.forEach(function(d) {
			var y0 = 0;
			d.values = color.domain().map(function(name) { return {name: name, y0: y0, y1: Number(d3.format('.2f')(y0 = d[name]))}; });
			d.total =  Number(d3.format('.2f')(d.values[d.values.length - 1].y1));
		});

		yScale
			.domain([0, 1.15 * d3.max(dataJSON.frequency, function(d) {return d.highest;})]);

		// add main group
		var mainGroup = svg.append('g')
			.attr('class', 'bar-chart')
			.attr("transform", "translate(" + leftOffsetAxis + "," + topOffset + ")");

		// add axis
		mainGroup.append("g")
			.attr("class", "x axis")
			.attr("transform", "translate(0," + (height + bottomOffsetAxis/4) + ")")
			.call(xAxis);

		mainGroup.append("g")
			.attr("class", "y axis")
			.call(yAxis);

		// lines
		mainGroup.selectAll("g.y.axis g.tick")
			.append("line")
			.classed("grid-line", true)
			.attr("x1", offsetLeft)
			.attr("y1", 0)
			.attr("x2", width)
			.attr("y2", 0)
			.style("stroke", '#aeadae');

		// add bars group
		var barGroups = mainGroup
			.selectAll('g.bar')
			.data(dataJSON.frequency)
			.enter()
			.append('g')
			.attr('class', 'bar')
			.attr("transform", function(d) { return "translate(" + xScale(d.month) + ",0)"; });

		// add bars
		barGroups.selectAll('rect')
			.data(function(d) { return d.values; })
			.enter().append("rect")
			.attr("width", xScale.rangeBand())
			.attr("y", function(d) {return yScale(d.y1); })
			.attr("height", function(d) { return yScale(d.y0) - yScale(d.y1); })
			.attr("fill", function(d) { return color(d.name); });

		// set external values
		var lowestValue = d3.select('#lowest-value');
		if(lowestValue.length) {
			lowestValue.text(d3.min(dataJSON.frequency, function(d) {return d.lowest;}));
		}

		var highestValue = d3.select('#highest-value');
		if(highestValue.length) {
			highestValue.text(d3.max(dataJSON.frequency, function(d) {return d.highest;}));
		}

		// resize handler
		var chartHolder = svg.select('.bar-chart');
		var ratio = chartHolder.node().getBoundingClientRect().width / chartHolder.node().getBoundingClientRect().height;
		d3.select(window)
			.on('resize.bar-chart', function() {
				svg
					.attr('height', function() {
						return svg.node().getBoundingClientRect().width / ratio;
					});
			});

		svg
			.attr('height', function() {
				return svg.node().getBoundingClientRect().width / ratio;
			});
	});

}

// donut chart post types
function initPostTypesChart() {
	var holder = d3.select('#donut-chart-post-types');
	if (!holder.node()) return;

	var dataUrl = holder.attr('data-json');
	var dataJSON;
	var width = 700;
	var height = 700;
	var radius = 558 / 2;
	var offetLabels = 120;
	var angleMainCircle = 25;
	var bigCircleColor = '#ccc';
	var bigCircleStrokeWidth = 2;
	var middleCircleColor = '#3a5897';
	var middleCircleStrokeWidth = 3;
	var middleCircleOffset = 18;
	var rightAngle = 90;

	// create layout
	var pie = d3.layout.pie()
		.value(function(d) { return d.value; })
		.sort(null);

	var arc = d3.svg.arc()
		.innerRadius(radius - 130)
		.outerRadius(radius - 33);

	// add main svg
	var svg = holder.append('svg')
		.attr('width', width)
		.attr('height', height)
		.attr('preserveAspectRatio', 'xMidYMid meet')
		.attr('viewBox', '0 0 ' + width + ' ' + height);

	var mainGroup = svg
		.append('g')
		.attr('class', 'chart')
		.attr('transform', 'translate(' + width / 2 + ',' + height / 2 + ') rotate(' + angleMainCircle + ' 0 0)');

	d3.json(dataUrl, function (error, json) {
		
		
		// Populate most frequent post percentage fields
		var mostFrequentPostTypeObject = JSON.parse(mostFrequentPostTypeData);
		
		$('#most-frequent-post-percentage').html(mostFrequentPostTypeObject.percentage + '%');
		$('#most-frequent-post-type').html(mostFrequentPostTypeObject.type);
		
		$('#most-frequent-post-percentage').addClass(mostFrequentPostTypeObject.color);
		$('#most-frequent-post-type').addClass(mostFrequentPostTypeObject.color);
		
		
		if (error) return console.warn(error);
//		dataJSON = json[0];
		dataJSON = JSON.parse(postTypesData);

		// set chart legend
		var listItems = d3.selectAll('#post-types-list');

		var items = listItems.selectAll('li')
			.data(dataJSON.types)
			.enter()
			.append('li');

		items = listItems.selectAll('li')
			.style('color', function(d) {
				if (d) {return d.color;}
			})
			.text('');

//		items
//			.append('span')
//			.attr('class', 'sing')
//			.text(function(d) {
//				if(d) {return d.type + ' - ';}
//			});

		items
			.append('span')
			.attr('class', 'description')
			.text(function(d) {
				if (d) {return d.description;}
				
			});

		items.data(dataJSON.types).exit().remove();

		// add group arc
		var g = mainGroup.selectAll(".arc")
			.data(pie(dataJSON.types))
			.enter().append('g')
			.attr('class', 'arc');

		// add arc
		g.append('path')
			.attr('d', arc)
			.attr('fill', function(d, i) {
				return dataJSON.types[i].color;
			});

		// add group label
		var label = g.append('g')
			.attr('class', 'label')
			.attr('transform', function(d) {
				var arcRadius = 100;
				d.outerRadius = arcRadius + 10;
				d.innerRadius = arcRadius + 15;
				var rotateAngle = (d.startAngle + d.endAngle) / 2 * (180 / Math.PI);
				if ((d.startAngle + d.endAngle) / 2 < Math.PI) {
					rotateAngle -= rightAngle;
				} else {
					rotateAngle += rightAngle;
				}
				return 'translate(' + arc.centroid(d) + ') rotate(' + rotateAngle + ')';
			});

		// add label
		label.append('text')
			.attr('class', 'type-label')
			.attr('dx', function(d) {
				if ((d.startAngle + d.endAngle)/2 < Math.PI) {
					return offetLabels - 10;
				} else {
					return -offetLabels + 10;
				}
			})
			.attr('transform', function(d) {
				var arcRadius = 100;
				d.outerRadius = arcRadius + 10;
				d.innerRadius = arcRadius + 15;
				var rotateAngle = (d.startAngle + d.endAngle) / 2 * (180 / Math.PI);
				var rotateValue = offetLabels;
				if ((d.startAngle+d.endAngle)/2 < Math.PI) {
					rotateAngle -= rightAngle;
					rotateValue = -rotateValue;
				} else {
					rotateAngle += rightAngle;
				}
				return 'rotate(' + -(rotateAngle + angleMainCircle) + ' ' + -rotateValue + ' ' + 0 + ')';
			})
			.attr('dy', '.35em')
			.attr('fill', function(d, i) {
				return dataJSON.types[i].color;
			})
			.style('text-anchor', function(d) {return (d.startAngle + d.endAngle) / 2 < Math.PI ? 'start' : 'end';})
			.text(function(d, i) {
				return dataJSON.types[i].type;
			});

		// add decor circles
		var decorGroup = svg.append('g')
			.attr('class', 'decor');

		// big circle
		var bigCircle = decorGroup
			.append('circle')
			.attr('cx', width / 2)
			.attr('cy', height / 2)
			.attr('r', radius)
			.style('fill', 'none')
			.style('stroke', bigCircleColor)
			.style('stroke-width', bigCircleStrokeWidth);

		var middleCircle = decorGroup
			.append('circle')
			.attr('cx', width / 2)
			.attr('cy', height / 2)
			.attr('r', radius - middleCircleOffset)
			.attr('stroke-dasharray', '7, 7')
			.style('fill', 'none')
			.style('stroke', middleCircleColor)
			.style('stroke-width', middleCircleStrokeWidth);

		// resize handler
		var chartHolder = svg.select('.decor');
		var ratio = chartHolder.node().getBoundingClientRect().width / chartHolder.node().getBoundingClientRect().height;
		d3.select(window)
			.on('resize.donut', function() {
				svg
					.attr('height', function() {
						return svg.node().getBoundingClientRect().width / ratio;
					});
			});

		svg
			.attr('height', function() {
				return svg.node().getBoundingClientRect().width / ratio;
			});
	});
}

// bar chart post likes
function initFriendsChart() {
	var holder = d3.select('#friend-chart');
	if (!holder.node()) return;

	var dataUrl = holder.attr('data-json');
	var dataJSON;
	var width = 670;
	var height = 400;
	var barHeight = 66;
	var barsOffset = 4;
	var circleOffsetLeft = 19;
	var circleOffsetTop = 3;
	var textOffsetLeft = 12;
	var circleRadius = barHeight / 2 - circleOffsetTop;
	var fontSideName = 20;
	var fontSideLikes = 22;
	var likesOffsetRight = 19;
	var offserBetweenText = 5;

	d3.json(dataUrl, function (error, json) {
		if (error) return console.warn(error);
//		dataJSON = json[0];
		dataJSON = JSON.parse(topFriendsData);

		// set friends amount
		var friendsAmount = d3.select('#friends-amount');
		if(friendsAmount.length) {
			friendsAmount.text(dataJSON.friends.length);
		}

		// set friends likes
		var friendsLikes = d3.select('#friends-likes');
		if(friendsLikes.length) {
			friendsLikes.text(d3.sum(dataJSON.friends, function(d) {return d.likes;}));
		}

		// set height from data
		height = (barHeight + barsOffset) * dataJSON.friends.length;

		// add main svg
		var svg = holder.append('svg')
			.attr('width', width)
			.attr('height', height)
			.attr('preserveAspectRatio', 'xMidYMid meet')
			.attr('viewBox', '0 0 ' + width + ' ' + height);

		// set photo attr
		var defs = svg.append('defs');

		defs.selectAll('pattern')
			.data(dataJSON.friends)
			.enter()
			.append('pattern')
			.attr('id', function(d, i) {
				return 'chart-image-' + i;
			})
			.attr('patternUnits', 'userSpaceOnUse')
			.attr('x', circleOffsetLeft)
			.attr('y',  function(d, i) {
				return i * (barHeight + barsOffset) + circleOffsetTop;
			})
			.attr('height', 60)
			.attr('width', 60)
			.append('svg:image')
			.attr('xlink:href', function(d) {
				return d.imgSrc;
			})
			.attr('x', 0)
			.attr('y', 0)
			.attr('height', 60)
			.attr('width', 60);

		// add groups
		var barsArea = svg.append('g')
			.attr('class', 'bars')
			.attr('x', 0)
			.attr('y', 0);

		var bars = barsArea.selectAll('g')
			.data(dataJSON.friends)
			.enter()
			.append('g');

		// add bars
		bars
			.append('rect')
			.attr('height', barHeight)
			.attr('width', 300)
			.attr('y', function(d, i) {
				return i * (barHeight + barsOffset);
			})
			.attr('x', 0)
			.attr('fill', function(d) {
				return d.color;
			});

		// add circles with photos
		bars
			.append('circle')
			.attr('cx', circleOffsetLeft + circleRadius)
			.attr('cy', function(d, i) {
				return i * (barHeight + barsOffset) + circleRadius + circleOffsetTop;
			})
			.attr('r', circleRadius)
			.attr('fill', function(d, i) {
				return 'url(#chart-image-' + i +')';
			});

		// add name
		bars
			.append('text')
			.attr('class', 'name')
			.attr('fill', '#fff')
			.attr('x', function() {
				return circleRadius * 2 + circleOffsetLeft + textOffsetLeft;
			})
			.attr('y',  function(d, i) {
				return i * (barHeight + barsOffset) + circleRadius + circleOffsetTop + fontSideName / 3;
			})
			.style('fontSize', fontSideName)
			.text(function(d) {
				return d.name;
			});

		// add amount
		bars
			.append('text')
			.attr('class', 'likes')
			.attr('fill', '#fff')
			.text('asdasdas')
			.attr('x', function() {
				return circleRadius * 2 + circleOffsetLeft + textOffsetLeft + 200;
			})
			
			.attr('y',  function(d, i) {
				return i * (barHeight + barsOffset) + circleRadius + circleOffsetTop + fontSideLikes / 3;
			})
			.text(function(d) {
				if(d.likes === 1) {
					return d.likes + ' like';
				} else {
					return d.likes + ' likes';
				}
			});

		// calculate scale
		var maxNameWidth = d3.max(bars.selectAll('text.name'), function(d) {return Math.ceil(d[0].getComputedTextLength());});
		var maxTextLikesWidth = d3.max(bars.selectAll('text.likes'), function(d) {return Math.ceil(d[0].getComputedTextLength());});
		var minConstantWidth = maxNameWidth + maxTextLikesWidth + circleOffsetLeft + textOffsetLeft + circleRadius * 2 + likesOffsetRight + offserBetweenText;

		// create scale chart 
		var likesScale = d3.scale.linear()
			.domain([0, d3.max(dataJSON.friends, function(d) {return d.likes;})])
			.range([minConstantWidth, width]);

		// set finish size bars
		bars
			.selectAll('rect')
			.attr('width', function(d) {return likesScale(d.likes);});

		// set finish position text likes
		bars
			.selectAll('text.likes')
			.attr('x', function(d) {
				return minConstantWidth - likesOffsetRight + (width - minConstantWidth) - (width - likesScale(d.likes));
			});

		// resize handler
		var chartHolder = svg.select('.bars');
		var ratio = chartHolder.node().getBoundingClientRect().width / chartHolder.node().getBoundingClientRect().height;
		d3.select(window)
			.on('resize.likes', function() {
				svg
					.attr('height', function() {
						return svg.node().getBoundingClientRect().width / ratio;
					});
			});

		svg
			.attr('height', function() {
				return svg.node().getBoundingClientRect().width / ratio;
			});
	});
}

// align blocks height
function initSameHeight() {
	jQuery('section.block-popular').sameHeight({
		elements: 'div.box-holder',
		flexible: true,
		multiLine: true
	});
}

function initParallaxBg() {
	// detect device type
	var isTouchDevice = /Windows Phone/.test(navigator.userAgent) || ('ontouchstart' in window) || window.DocumentTouch && document instanceof DocumentTouch;
	if (!isTouchDevice) {
		jQuery('.bg-holder').parallaxBG({
			parent: '.bg-frame',
			image: 'img',
			parallaxOffset: 100,
			fallbackFunc: initBgStretch
		});
	} else {
		jQuery('.bg-frame').each(function() {
			ImageStretcher.add({
				container: this,
				image: 'img'
			});
		});
	}
}


// fade blocks init
function initFadeBlock() {
	jQuery('.fade-block').each(function() {
		var box = jQuery(this);
		box.fadeBlock({
			fixedClass: 'fade-active',
			compareBlock: box,
			customCompare: true
		});
	});
}


/*
 * Image Stretch module
 */
var ImageStretcher = {
	getDimensions: function(data) {
		// calculate element coords to fit in mask
		var ratio = data.imageRatio || (data.imageWidth / data.imageHeight),
			slideWidth = data.maskWidth,
			slideHeight = slideWidth / ratio;

		if(slideHeight < data.maskHeight) {
			slideHeight = data.maskHeight;
			slideWidth = slideHeight * ratio;
		}
		return {
			width: slideWidth,
			height: slideHeight,
			top: (data.maskHeight - slideHeight) / 2,
			left: (data.maskWidth - slideWidth) / 2
		};
	},
	getRatio: function(image) {
		if(image.prop('naturalWidth')) {
			return image.prop('naturalWidth') / image.prop('naturalHeight');
		} else {
			var img = new Image();
			img.src = image.prop('src');
			return img.width / img.height;
		}
	},
	imageLoaded: function(image, callback) {
		var self = this;
		var loadHandler = function() {
			callback.call(self);
		};
		if(image.prop('complete')) {
			loadHandler();
		} else {
			image.one('load', loadHandler);
		}
	},
	resizeHandler: function() {
		var self = this;
		jQuery.each(this.imgList, function(index, item) {
			if(item.image.prop('complete')) {
				self.resizeImage(item.image, item.container);
			}
		});
	},
	resizeImage: function(image, container) {
		this.imageLoaded(image, function() {
			var styles = this.getDimensions({
				imageRatio: this.getRatio(image),
				maskWidth: container.width(),
				maskHeight: container.height()
			});
			image.css({
				width: styles.width,
				height: styles.height,
				marginTop: styles.top,
				marginLeft: styles.left
			});
		});
	},
	add: function(options) {
		var container = jQuery(options.container ? options.container : window),
			image = typeof options.image === 'string' ? container.find(options.image) : jQuery(options.image);

		// resize image
		this.resizeImage(image, container);

		// add resize handler once if needed
		if(!this.win) {
			this.resizeHandler = jQuery.proxy(this.resizeHandler, this);
			this.imgList = [];
			this.win = jQuery(window);
			this.win.on('resize orientationchange', this.resizeHandler);
		}

		// store item in collection
		this.imgList.push({
			container: container,
			image: image
		});
	}
};

/*
 * fade block plugin
 */
;(function($) {
	function FadeBlock(options) {
		this.options = $.extend({
			holder: null,
			fixedClass: 'fixed',
			compareBlock: 'div',
			blockHeight: false,
			blockTop: false,
			heightRatio: false,
			customCompare: false,
			oneLoad: false
		}, options);
		this.init();
	}
	FadeBlock.prototype = {
		init: function() {
			if (this.options.holder) {
				this.findElements();
				this.attachEvents();
			}
		},
		findElements: function() {
			this.holder = jQuery(this.options.holder);
			this.compareBlock = jQuery(this.options.compareBlock);
			this.win = jQuery(window);

			if (this.compareBlock.length) {
				if (this.options.blockHeight){
					this.scrollHeight = this.compareBlock.innerHeight();
				}

				if (this.options.blockTop){
					this.scrollHeight  = this.compareBlock.offset().top;
				}
			}

			if (this.options.heightRatio){
				this.scrollHeight  = this.options.heightRatio;
			}
		},
		attachEvents: function() {
			// bind handlers scope
			this.bindHandlers(['onScroll']);
			this.win.bind('scroll.stickyBlock', this.onScroll);
			this.onScroll();
		},
		onScroll: function() {
			var self = this;
			self.scrollTop = self.win.scrollTop();
			if (self.options.customCompare) {
				if ((self.scrollTop + self.win.height()) > self.compareBlock.offset().top + self.compareBlock.innerHeight()/3) {
					self.holder.addClass(self.options.fixedClass);
				} else {
					if (!self.options.oneLoad) {
						self.holder.removeClass(self.options.fixedClass);
					}
				}
			} else {
				if (self.scrollTop > self.scrollHeight){
					self.holder.addClass(self.options.fixedClass);
					self.makeCallback('onFixed', self);
				} else {
					self.holder.removeClass(self.options.fixedClass);
					self.makeCallback('notFixed', self);
				}
			}
		},
		bindHandlers: function(handlersList) {
			var self = this;
			$.each(handlersList, function(index, handler) {
				var origHandler = self[handler];
				self[handler] = function() {
					return origHandler.apply(self, arguments);
				};
			});
		},
		makeCallback: function(name) {
			if (typeof this.options[name] === 'function') {
				var args = Array.prototype.slice.call(arguments);
				args.shift();
				this.options[name].apply(this, args);
			}
		}
	};

	// jQuery plugin interface
	$.fn.fadeBlock = function(opt) {
		return this.each(function() {
			new FadeBlock($.extend(opt, { holder: this }));
		});
	};
}(jQuery));

;(function($){
	function ParallaxBG(opt) {
		this.options = $.extend({
			parent: '.bg-frame',
			image: 'img',
			parallaxOffset: 100,
			fallbackFunc: function() {}
		}, opt);
		this.init();
	}
	ParallaxBG.prototype = {
		init: function() {
			if(this.options.holder) {
				if(typeof this.options.fallbackFunc == 'function' && (oldIe || isTouchDevice)) {
					this.options.fallbackFunc();
					return;
				}
				this.getStructure();
				this.attachEvents();
			}
		},
		getStructure: function() {
			this.holder = $(this.options.holder);
			this.parent = this.holder.find(this.options.parent);
			this.holderHeight = this.holder.height();
			this.holderOffset = this.holder.offset().top;

			// generate bg
			this.image = this.parent.find(this.options.image).eq(0).css({visibility: 'hidden'});
			this.imageRatio = this.image.attr('width')/this.image.attr('height') || this.image.width()/this.image.height();

			this.parent.css({
				backgroundImage: 'url('+this.image.attr('src')+')',
				backgroundRepeat: 'no-repeat',
				backgroundAttachment: 'fixed'
			});

			this.win = $(window);
			this.winHeight = this.win.height();
			this.winWidth = this.win.width();
			this.winScroll = this.win.scrollTop();
			this.bgHeight = this.winHeight + this.options.parallaxOffset;
		},
		attachEvents: function() {
			var self = this;
			this.bindHandlers(['scrollLayout']);
			this.bindHandlers(['resizeLayout']);
			this.win.bind('scroll', this.scrollLayout).bind('resize load', this.resizeLayout);

			// fix load problem
			setTimeout(function() {
				self.resizeLayout();
				self.win.trigger('scroll');
			}, 500);
		},
		resizeLayout: function() {
			// get dimensions
			this.winHeight = this.win.height();
			this.winWidth = this.win.width();
			this.holderHeight = this.holder.height();
			this.holderOffset = this.holder.offset().top;
			
			this.blockHeight = this.winHeight + this.options.parallaxOffset;
			this.currentTop = Math.max(0, this.blockHeight - this.holderHeight);

			// get parallax ratio and image ratio state
			this.parallaxRatio = this.win.width()/(this.winHeight + this.options.parallaxOffset);
			this.ratioState = this.imageRatio <= this.parallaxRatio;
			
			if(this.ratioState) {
				this.bgWidth = this.winWidth;
				this.bgHeight = this.bgWidth/this.imageRatio;
			} else {
				this.bgWidth = 'auto';
				this.bgHeight = this.winHeight + this.options.parallaxOffset;
			}

			this.parent.css({
				paddingBottom: this.currentTop,
				backgroundSize: this.bgWidth != 'auto' ? this.bgWidth +'px '+this.bgHeight+'px' : this.bgWidth +' '+this.bgHeight+'px'
			});
			this.scrollLayout();
		},
		scrollLayout: function() {
			this.winScroll = this.win.scrollTop();
			this.offsetPercentage = Math.max(0, Math.min((this.winScroll + this.winHeight - this.holderOffset)/(this.winHeight + this.holderHeight), 1)).toFixed(4);

			if(!!this.ratioState) {
				var curPos = '50% ' + ((-parseFloat(this.offsetPercentage)*this.options.parallaxOffset) - (this.bgHeight - this.winHeight)/2)+'px';
			} else {
				var curPos = '50% ' + (-parseFloat(this.offsetPercentage)*this.options.parallaxOffset)+'px';
			}

			this.parent.css({backgroundPosition: curPos});
		},
		bindHandlers: function(handlersList) {
			var self = this;
			$.each(handlersList, function(index, handler) {
				var origHandler = self[handler];
				self[handler] = function() {
					return origHandler.apply(self, arguments);
				};
			});
		}
	};

	// detect device type
	var isTouchDevice = /MSIE 10.*Touch/.test(navigator.userAgent) || ('ontouchstart' in window) || window.DocumentTouch && document instanceof DocumentTouch;
	var oldIe = window.attachEvent && !window.addEventListener;

	// jquery parallax plugin
	$.fn.parallaxBG = function(opt){
		return this.each(function(){
			new ParallaxBG($.extend(opt,{holder:this}));
		});
	};
}(jQuery));

// background stretching
function initBgStretch() {
	jQuery('.bg-frame').each(function() {
		var holder = jQuery(this);
		var image = holder.find('img');
		jQuery(window).bind('load resize', function() {
			var settings = getProportions(getDimensions(image, holder));
			resizeAll(image, settings);	
		});
	});

	function getProportions(dimensions) {
		var ratio = dimensions.ratio || (dimensions.elementWidth / dimensions.elementHeight);
		var slideWidth = dimensions.maskWidth, slideHeight = slideWidth / ratio;
		if(slideHeight < dimensions.maskHeight) {
			slideHeight = dimensions.maskHeight;
			slideWidth = slideHeight * ratio;
		}
		return {
			width: slideWidth,
			height: slideHeight,
			top: (dimensions.maskHeight - slideHeight) / 2,
			left: (dimensions.maskWidth - slideWidth) / 2
		}
	}
	
	function getDimensions(img, block) {
		img.css({
			height: '',
			left: '',
			top: '',
			width: ''
		});
		return {
			ratio: img.width()/img.height(),
			maskWidth: block.width(),
			maskHeight: block.outerHeight(true)
		};
	}

	function resizeAll(img, obj) {
		img.css({
			height: obj.height,
			left: obj.left,
			top: obj.top,
			width: obj.width
		});
	}
}

/*
 * jQuery SameHeight plugin
 */
;(function($){
	$.fn.sameHeight = function(opt) {
		var options = $.extend({
			skipClass: 'same-height-ignore',
			leftEdgeClass: 'same-height-left',
			rightEdgeClass: 'same-height-right',
			elements: '>*',
			flexible: false,
			multiLine: false,
			useMinHeight: false,
			biggestHeight: false
		},opt);
		return this.each(function(){
			var holder = $(this), postResizeTimer, ignoreResize;
			var elements = holder.find(options.elements).not('.' + options.skipClass);
			if(!elements.length) return;

			// resize handler
			function doResize() {
				elements.css(options.useMinHeight && supportMinHeight ? 'minHeight' : 'height', '');
				if(options.multiLine) {
					// resize elements row by row
					resizeElementsByRows(elements, options);
				} else {
					// resize elements by holder
					resizeElements(elements, holder, options);
				}
			}
			doResize();

			// handle flexible layout / font resize
			var delayedResizeHandler = function() {
				if(!ignoreResize) {
					ignoreResize = true;
					doResize();
					clearTimeout(postResizeTimer);
					postResizeTimer = setTimeout(function() {
						doResize();
						setTimeout(function(){
							ignoreResize = false;
						}, 10);
					}, 100);
				}
			};

			// handle flexible/responsive layout
			if(options.flexible) {
				$(window).bind('resize orientationchange fontresize', delayedResizeHandler);
			}

			// handle complete page load including images and fonts
			$(window).bind('load', delayedResizeHandler);
		});
	};

	// detect css min-height support
	var supportMinHeight = typeof document.documentElement.style.maxHeight !== 'undefined';

	// get elements by rows
	function resizeElementsByRows(boxes, options) {
		var currentRow = $(), maxHeight, maxCalcHeight = 0, firstOffset = boxes.eq(0).offset().top;
		boxes.each(function(ind){
			var curItem = $(this);
			if(curItem.offset().top === firstOffset) {
				currentRow = currentRow.add(this);
			} else {
				maxHeight = getMaxHeight(currentRow);
				maxCalcHeight = Math.max(maxCalcHeight, resizeElements(currentRow, maxHeight, options));
				currentRow = curItem;
				firstOffset = curItem.offset().top;
			}
		});
		if(currentRow.length) {
			maxHeight = getMaxHeight(currentRow);
			maxCalcHeight = Math.max(maxCalcHeight, resizeElements(currentRow, maxHeight, options));
		}
		if(options.biggestHeight) {
			boxes.css(options.useMinHeight && supportMinHeight ? 'minHeight' : 'height', maxCalcHeight);
		}
	}

	// calculate max element height
	function getMaxHeight(boxes) {
		var maxHeight = 0;
		boxes.each(function(){
			maxHeight = Math.max(maxHeight, $(this).outerHeight());
		});
		return maxHeight;
	}

	// resize helper function
	function resizeElements(boxes, parent, options) {
		var calcHeight;
		var parentHeight = typeof parent === 'number' ? parent : parent.height();
		boxes.removeClass(options.leftEdgeClass).removeClass(options.rightEdgeClass).each(function(i){
			var element = $(this);
			var depthDiffHeight = 0;
			var isBorderBox = element.css('boxSizing') === 'border-box' || element.css('-moz-box-sizing') === 'border-box' || element.css('-webkit-box-sizing') === 'border-box';

			if(typeof parent !== 'number') {
				element.parents().each(function(){
					var tmpParent = $(this);
					if(parent.is(this)) {
						return false;
					} else {
						depthDiffHeight += tmpParent.outerHeight() - tmpParent.height();
					}
				});
			}
			calcHeight = parentHeight - depthDiffHeight;
			calcHeight -= isBorderBox ? 0 : element.outerHeight() - element.height();

			if(calcHeight > 0) {
				element.css(options.useMinHeight && supportMinHeight ? 'minHeight' : 'height', calcHeight);
			}
		});
		boxes.filter(':first').addClass(options.leftEdgeClass);
		boxes.filter(':last').addClass(options.rightEdgeClass);
		return calcHeight;
	}
}(jQuery));

/*
 * jQuery FontResize Event
 */
jQuery.onFontResize = (function($) {
	$(function() {
		var randomID = 'font-resize-frame-' + Math.floor(Math.random() * 1000);
		var resizeFrame = $('<iframe>').attr('id', randomID).addClass('font-resize-helper');

		// required styles
		resizeFrame.css({
			width: '100em',
			height: '10px',
			position: 'absolute',
			borderWidth: 0,
			top: '-9999px',
			left: '-9999px'
		}).appendTo('body');

		// use native IE resize event if possible
		if (window.attachEvent && !window.addEventListener) {
			resizeFrame.bind('resize', function () {
				$.onFontResize.trigger(resizeFrame[0].offsetWidth / 100);
			});
		}
		// use script inside the iframe to detect resize for other browsers
		else {
			var doc = resizeFrame[0].contentWindow.document;
			doc.open();
			doc.write('<scri' + 'pt>window.onload = function(){var em = parent.jQuery("#' + randomID + '")[0];window.onresize = function(){if(parent.jQuery.onFontResize){parent.jQuery.onFontResize.trigger(em.offsetWidth / 100);}}};</scri' + 'pt>');
			doc.close();
		}
		jQuery.onFontResize.initialSize = resizeFrame[0].offsetWidth / 100;
	});
	return {
		// public method, so it can be called from within the iframe
		trigger: function (em) {
			$(window).trigger("fontresize", [em]);
		}
	};
}(jQuery));