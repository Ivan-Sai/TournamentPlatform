import {Component, OnInit} from '@angular/core';
import {AsyncPipe, NgForOf, NgIf, NgOptimizedImage, NgStyle} from "@angular/common";
import {Observable} from "rxjs";
import {TournamentRequestData} from "../../../../models/request/TournamentRequestData";
import {TournamentService} from "../../../../services/TournamentService";
import {ActivatedRoute, Router} from "@angular/router";

@Component({
  selector: 'app-tournament-all-list',
  templateUrl: './tournament-all-list.component.html',
  standalone: true,
  imports: [
    NgForOf,
    NgStyle,
    NgIf,
    AsyncPipe,
    NgOptimizedImage
  ],
  styleUrls: ['./tournament-all-list.component.css']
})
export class TournamentAllListComponent implements OnInit{
  public tournaments$: Observable<TournamentRequestData[]> = this.tournamentService.getALlTournaments(null,null);
  public logoDataMap: { [key: number]: string } = {};
  public isHasLogoMap: {[key: number] : boolean} = {}



  constructor(private tournamentService: TournamentService,
              private router:Router,
              private route: ActivatedRoute) {
  }

  ngOnInit(): void {
    this.loadLogos()
    this.route.queryParams.subscribe(params => {
      const filter = params['filter'];
      const discipline = params['discipline'];
      this.tournaments$ = this.tournamentService.getALlTournaments(filter,discipline)
    });

  }


  loadLogos(): void {
    this.tournaments$.forEach(value => {
      value.forEach(value1 => {
        this.tournamentService.getTournamentLogo(value1.id).subscribe(
          value2 => {
            this.logoDataMap[value1.id] = 'data:image/jpeg;base64,' + value2.data
            this.isHasLogoMap[value1.id] = true;
          }
          ,error => {
            this.isHasLogoMap[value1.id] = false
          }
        )
      })
    })

  }

  redirectToPage(url: string) {
    this.router.navigateByUrl(url)
  }

  getLogo(id: number): string {
    return this.logoDataMap[id]
  }

  toLocalDate(data: Date) {
    return new Date(data).toLocaleString()
  }
}
