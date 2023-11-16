import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class BracketRefreshService {
  private bracketRefreshSource = new BehaviorSubject<boolean>(true);
  bracketRefresh$ = this.bracketRefreshSource.asObservable();

  refreshBracket() {
    this.bracketRefreshSource.next(true);
  }
}
