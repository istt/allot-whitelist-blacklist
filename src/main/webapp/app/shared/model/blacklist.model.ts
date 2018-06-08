export interface IBlacklist {
    id?: number;
    url?: string;
}

export class Blacklist implements IBlacklist {
    constructor(public id?: number, public url?: string) {}
}
