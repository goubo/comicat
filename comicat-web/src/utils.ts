export class Utils {
    static isSameArray = function (array1: any[], array2: any[]) {
        if (array1.length !== array2.length) return false;
        return array1.sort().join('') === array2.sort().join('');
    };
}
