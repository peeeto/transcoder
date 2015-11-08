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
                    './assets/js/main.js': ['./assets/js/libs/*.js']
                }
            }
        },
        uglify: {
            my_target: {
                files: {
                    './assets/js/main.min.js': ['./assets/js/main.js']
                }
            },
            options: {}
        },
        'node-qunit': {
            deps: './assets/js/main.js',
            code: './assets/js/main.js',
            tests: './test/test.js',
            done: function (err, res) {
                !err && publishResults("node", res, this.async());
            }
        }
    });

    grunt.loadNpmTasks('grunt-node-qunit');
    grunt.loadNpmTasks('grunt-contrib-uglify'); // load the given tasks
    grunt.loadNpmTasks('grunt-contrib-concat');
    grunt.registerTask('default', ['concat', 'uglify', 'node-qunit']); // Default grunt tasks maps to grunt
};

