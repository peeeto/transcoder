module.exports = function (grunt) {
    grunt.initConfig({
        pkg: grunt.file.readJSON('package.json'), // the package file to use
        concat: {
            js: {
                options: {
                    block: true,
                    line: true,
                    stripBanners: true
                },
                files: {
                    './assets/js/dist/main.js': ['./assets/js/libs/*.js']
                }
            }
        },
        uglify: {
            my_target: {
                files: {
                    './assets/js/dist/main.min.js': ['./assets/js/dist/main.js', './assets/js/functions.js']
                }
            },
            options: {
                preserveComments: false,
                report: 'gzip',
                compress: true
            }
        },
        replace: {
            dist: {
                options: {
                    patterns: [
                        {
                            match: /\.\.\/js/g,
                            replacement: function () {
                                return 'assets/js';
                            }
                        },
                        {
                            match: /\.\.\/css/g,
                            replacement: function () {
                                return 'assets/css';
                            }
                        },
                        {
                            match: 'timestamp',
                            replacement: '<%= new Date().getTime() %>'
                        }
                    ]
                },
                files: [
                    {src: ['assets/templates/index.html'], dest: './index.html'},
                    {src: ['assets/templates/qunit.html'], dest: './qunit.html'}
                ]
            }
        }
        //'node-qunit': {
        //    deps: './assets/js/main.js',
        //    code: './assets/js/main.js',
        //    tests: './test/test.js',
        //    done: function (err, res) {
        //        !err && publishResults("node", res, this.async());
        //    }
        //}
    });

//    grunt.loadNpmTasks('grunt-node-qunit');
    grunt.loadNpmTasks('grunt-contrib-uglify');
    grunt.loadNpmTasks('grunt-contrib-concat');
    grunt.loadNpmTasks('grunt-replace');
    grunt.registerTask('default', ['concat', 'uglify', 'replace']);
};

