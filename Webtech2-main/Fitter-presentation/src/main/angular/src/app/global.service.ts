import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class GlobalService {
  private userLoggedIn = new BehaviorSubject<boolean>(false);

  constructor() {}

  get isLoggedIn() {
    return this.userLoggedIn.asObservable();
  }

  login() {
    this.userLoggedIn.next(true);
  }

  logout() {
    this.userLoggedIn.next(false);
  }
}
