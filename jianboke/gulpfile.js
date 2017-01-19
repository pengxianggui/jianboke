"use strict";

var gulp = require('gulp'), //将上面down下来的插件都要引入，以便需要
    gutil = require('gulp-util'),
    sass = require('gulp-sass'),
    wiredep = require('wiredep').stream,
    flatten = require('gulp-flatten'), //删除或替换文件的相对路径
    proxy = require('http-proxy-middleware'), // proxy代理便于自动刷新
    browserSync = require('browser-sync').create(); // 浏览器自动刷新

/*
 * 执行将bower_components下插件引入index.html中的任务
 */
gulp.task('html', function() {  // 创建task任务:可以在cmd命令中执行任务
  gulp.src('src/main/webapp/index.html')
    .pipe(wiredep({  // 调用插件wiredep执行方法
      optional: 'configuration',
      goes: 'here'
    }))
    .pipe(gulp.dest('src/main/webapp'));
});

/*
 * 执行sass编译任务
 */
gulp.task('sass', function() {
	return gulp.src('src/main/scss/*.scss')
		.pipe(sass()) //scss编译为scss的核心调用
		.pipe(gulp.dest('src/main/webapp/style'));
});

/**
 * 原以为和material-icon有关系，但是貌似注释掉也可以正常显示icon
 */
//gulp.task('fonts', function() {
//  return gulp.src('src/main/webapp/**/*.{woff,woff2,ttf,eot}')
//    .pipe(flatten())
//    .pipe(gulp.dest('target/dist/' + 'fonts/'));
//});

/**
 * 创建watch任务
 * 1. 监听scss文件，一旦改变，自动编译为css文件
 * 2. 监听webapp下的自定义js、html和css文件，一旦改变自动刷新浏览器
 */
gulp.task('watch', function() {
//	gulp.watch('bower.json', ['wiredep', 'fonts']);
	gulp.watch('src/main/scss/*.scss', ['sass']); //监听scss下所有后缀为.scss的文件，对它们执行sass任务
	gulp.watch([
	    'src/main/webapp/scripts/**',
	    'src/main/webapp/style/**',
	    'src/main/webapp/views/**',
	    'src/main/webapp/*.html'
	]).on('change', browserSync.reload); //监听webapp目录下一些文件的改动，改动则重新加载浏览器自动刷新
	// 还可以执行其他监听任务
});

/**
 * 创建serve任务,该任务会先执行'wiredep'、'sass'和'watch'任务，然后再
 * 1. 启用代理窗口。用于监听并刷新浏览器。
 * 问题： material-icon在代理窗口下未生效。和'fonts'任务有关系。未解决
 */
gulp.task('serve', ['html', 'sass', 'watch'],  function() {
	var baseUri = 'http://localhost:' + 8089;
	browserSync.init({
		open: true,
		port: 9004,
		server: {
			baseDir: 'src/main/webapp/',
			middleware: [
	             proxy(baseUri + '/api'),
	             proxy(baseUri + '/assets'),
	             proxy(baseUri + '/manage'),
	             proxy(baseUri + '/h2-console'),
	             proxy(baseUri + '/v2/api-docs'),
	             proxy(baseUri + '/test'),
	             proxy(baseUri + '/static')
			]
		}
	})
})