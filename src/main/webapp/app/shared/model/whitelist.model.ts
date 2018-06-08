export interface IWhitelist {
    id?: number;
    url?: string;
}

export class Whitelist implements IWhitelist {
    constructor(public id?: number, public url?: string) {}
}
