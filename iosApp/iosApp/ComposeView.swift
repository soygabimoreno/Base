import Foundation
import SwiftUI
import shared

struct ComposeView: UIViewControllerRepresentable {
    func updateUIViewController(_ uiViewController: UIViewControllerType, context: Context) {
        // no-op
    }

    func makeUIViewController(context: Context) -> some UIViewController {
        AppKt.MainViewController()
    }
}
