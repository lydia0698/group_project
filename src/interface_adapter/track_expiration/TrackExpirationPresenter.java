package interface_adapter.track_expiration;

import use_case.track_expiration.TrackExpirationOutputBoundary;
import use_case.track_expiration.TrackExpirationOutputData;

public class TrackExpirationPresenter implements TrackExpirationOutputBoundary{
    private final TrackExpirationViewModel viewModel;

    public TrackExpirationPresenter(
            TrackExpirationViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void presentExpirationResults(
            TrackExpirationOutputData outputData) {

        viewModel.setExpiredItems(
                outputData.getExpiredItems());

        viewModel.setExpiringSoonItems(
                outputData.getExpiringSoonItems());

        viewModel.setErrorMessage("");
    }

    @Override
    public void presentError(String message) {
        viewModel.setErrorMessage(message);
    }
}
