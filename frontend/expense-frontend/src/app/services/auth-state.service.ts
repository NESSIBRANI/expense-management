import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class AuthStateService {

  private _userReady = new BehaviorSubject<boolean>(
    !!localStorage.getItem('token') && !!localStorage.getItem('userId')
  );

  userReady$ = this._userReady.asObservable();

  setUserReady() {
    this._userReady.next(true);
  }

  reset() {
    this._userReady.next(false);
  }
}
