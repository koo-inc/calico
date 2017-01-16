var gulp = require('gulp');
var sass = require('gulp-sass');
var path = require('path');
var sourcemaps = require('gulp-sourcemaps');

var PROJECT = 'calico-sample';

function buildCompassTask(opts) {
  var root = opts.root;
  var from = path.join(root, opts.from);
  console.log('compile ' + from);
  gulp.src(from)
    .pipe(sourcemaps.init())
    .pipe(sass({
      //outputStyle: 'compressed',
      sourceComments: true,
      includePaths: [
        'src/main/webapp/assets/vendor/compass-mixins-1.0.0',
        'src/main/webapp/assets/vendor/bootstrap-sass-3.3.5/assets/stylesheets',
        'src/main/webapp/assets/vendor/bootstrap-additions-0.3.1/dist',
        'src/main/webapp/assets/vendor/angular-motion-0.4.2/dist',
        'src/main/webapp/assets/calico/styles'
      ]
    }))
    .pipe(sourcemaps.write('.', {sourceRoot: '.'}))
    .pipe(gulp.dest(root));
}

gulp.task('aggregateFonts', function() {
  gulp.src([
    'src/main/webapp/assets/vendor/bootstrap-sass-3.3.5/assets/fonts/**/*',
  ])
    .pipe(gulp.dest('src/main/webapp/assets/app/vendor-fonts'))
});

gulp.task('compassProjectResource', function() {
  buildCompassTask({
    root: 'src/main/webapp/assets/' + PROJECT,
    from: '**/*.scss'
  });
});

gulp.task('compassApp', function() {
  buildCompassTask({
    root: 'src/main/webapp/assets/app',
    from: '**/*.scss'
  });
});

gulp.task('compass', ['compassProjectResource', 'compassApp', 'aggregateFonts']);

gulp.task('compassWatch', ['compass'], function() {
  gulp.watch('src/main/webapp/assets/app/**/*.scss', ['compassApp']);
  gulp.watch('src/main/webapp/assets/' + PROJECT + '/**/*.scss', ['compassProjectResource']);
});
