package org.lotto.domain.resultchecker;

import lombok.AllArgsConstructor;
import org.lotto.domain.numbergenerator.WinningNumbersGeneratorFacade;
import org.lotto.domain.numbergenerator.dto.WinningNumbersDto;
import org.lotto.domain.numberreceiver.NumberReceieverFacade;
import org.lotto.domain.numberreceiver.dto.TicketDto;
import org.lotto.domain.resultchecker.Dto.PlayersDto;
import org.lotto.domain.resultchecker.Dto.ResultDto;

import java.util.List;
import java.util.Set;

import static org.lotto.domain.resultchecker.ResultCheckerMapper.mapPlayersToResults;

@AllArgsConstructor
public class ResultCheckerFacade {

    WinningNumbersGeneratorFacade winningNumbersGeneratorFacade;
    NumberReceieverFacade numberReceieverFacade;
    PlayerRepository playerRepository;
    WinnersRetriever winnerGenerator;


    public PlayersDto generateWinners() {
        List<TicketDto> allTicketsByDate = numberReceieverFacade.retrieveAllTicketsByNextDrawDate();
        List<Ticket> tickets = ResultCheckerMapper.mapFromTicketDto(allTicketsByDate);
        WinningNumbersDto winningNumbersDto = winningNumbersGeneratorFacade.generateWinningNumbers();
        Set<Integer> winningNumbers = winningNumbersDto.getWinningNumbers();
        if (winningNumbers == null || winningNumbers.isEmpty()) {
            return PlayersDto.builder()
                    .message("Winners failed to retrieve")
                    .build();
        }
        List<Player> players = winnerGenerator.retrieveWinners(tickets, winningNumbers);
        playerRepository.saveAll(players);
        return PlayersDto.builder()
                .results(mapPlayersToResults(players))
                .message("Winners succeeded to retrieve")
                .build();
    }

    public ResultDto findByHash(String hash) {
        Player player = playerRepository.findById(hash).orElseThrow(() -> new RuntimeException("Not found"));
        return ResultDto.builder()
                .hash(hash)
                .numbers(player.numbers())
                .hitNumbers(player.hitNumbers())
                .drawDate(player.drawDate())
                .isWinner(player.isWinner())
                .build();
    }
}